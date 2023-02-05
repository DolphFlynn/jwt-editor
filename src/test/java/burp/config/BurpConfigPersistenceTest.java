/*
Author : Dolph Flynn

Copyright 2022 Dolph Flynn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package burp.config;

import burp.api.montoya.persistence.Preferences;
import burp.intruder.FuzzLocation;
import burp.proxy.HighlightColor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static burp.config.BurpConfigPersistence.BURP_SETTINGS_NAME;
import static burp.intruder.FuzzLocation.HEADER;
import static burp.intruder.FuzzLocation.PAYLOAD;
import static burp.proxy.HighlightColor.CYAN;
import static burp.proxy.HighlightColor.RED;
import static burp.proxy.ProxyConfig.DEFAULT_HIGHLIGHT_COLOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BurpConfigPersistenceTest {
    private final Preferences callbacks = mock(Preferences.class);

    @Test
    void givenNoSavedConfig_whenLoadOrCreateCalled_thenDefaultInstanceReturned() {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isTrue();
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("name");
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(PAYLOAD);
        configPersistence.save(burpConfig);
    }

    private static Stream<String> invalidConfigJson() {
        return Stream.of(
                "",
                "{",
                "]",
                "true",
                "[]",
                "null",
                "448",
                "\"Edwards\""
        );
    }

    @ParameterizedTest
    @MethodSource("invalidConfigJson")
    void givenInvalidSavedConfig_whenLoadOrCreateCalled_thenDefaultConfigReturned(String invalidJson) {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(invalidJson);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isTrue();
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("name");
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(PAYLOAD);
    }

    private static Stream<String> invalidProxyConfigJson() {
        return Stream.of(
                "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":[]}",
                "{\"proxy_history_highlight_color\":\"red\"}",
                "{\"proxy_listener_enabled\":true}",
                "{\"proxy_history_highlight_color\":\"gunmetal\",\"proxy_listener_enabled\":true}",
                "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":\"turnip\"}"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidProxyConfigJson")
    void givenInvalidSavedProxyConfig_whenLoadOrCreateCalled_thenDefaultProxyConfigReturned(String invalidJson) {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(invalidJson);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isTrue();
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
    }

    private static Stream<Arguments> validProxyConfigJson() {
        return Stream.of(
                arguments(
                        "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":true}",
                        true,
                        RED
                ),
                arguments(
                        "{\"proxy_history_highlight_color\":\"cyan\",\"proxy_listener_enabled\":false}",
                        false,
                        CYAN
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validProxyConfigJson")
    void givenValidProxySavedConfig_whenLoadOrCreateCalled_thenAppropriateConfigReturned(String json, boolean listenerEnabled, HighlightColor highlightColor) {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isEqualTo(listenerEnabled);
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(highlightColor);
    }

    private static Stream<String> invalidIntruderConfigJson() {
        return Stream.of(
                "{\"intruder_payload_processor_fuzz_location\":\"header\"}",
                "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":[]}",
                "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":{}}}",
                "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":25519}}",
                "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":null}}",
                "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":true}}",
                "{\"intruder_payload_processor_parameter_name\":\"iss\"}",
                "{\"intruder_payload_processor_fuzz_location\":[],\"intruder_payload_processor_parameter_name\":\"iss\"}",
                "{\"intruder_payload_processor_fuzz_location\":{}},\"intruder_payload_processor_parameter_name\":\"iss\"}}",
                "{\"intruder_payload_processor_fuzz_location\":25519,\"intruder_payload_processor_parameter_name\":\"iss\"}}",
                "{\"intruder_payload_processor_fuzz_location\":false,\"intruder_payload_processor_parameter_name\":\"iss\"}}",
                "{\"intruder_payload_processor_fuzz_location\":null,\"intruder_payload_processor_parameter_name\":\"iss\"}}"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidIntruderConfigJson")
    void givenInvalidSavedIntruderConfig_whenLoadOrCreateCalled_thenDefaultInstanceReturned(String invalidJson) {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(invalidJson);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(PAYLOAD);
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("name");
    }

    private static Stream<Arguments> validIntruderConfigJson() {
        return Stream.of(
                arguments(
                        "{\"intruder_payload_processor_fuzz_location\":\"header\",\"intruder_payload_processor_parameter_name\":\"sub\"}",
                        HEADER,
                        "sub"
                ),
                arguments(
                        "{\"intruder_payload_processor_fuzz_location\":\"payload\",\"intruder_payload_processor_parameter_name\":\"role\"}",
                        PAYLOAD,
                        "role"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validIntruderConfigJson")
    void givenValidIntruderSavedConfig_whenLoadOrCreateCalled_thenAppropriateConfigReturned(String json, FuzzLocation expectedLocation, String expectedParameterName) {
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(expectedLocation);
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo(expectedParameterName);
    }

    @Test
    void givenValidProxyConfig_butInvalidIntruderConfig_whenLoadOrCreateCalled_thenAppropriateConfigReturned() {
        String json = "{\"proxy_history_highlight_color\":\"cyan\",\"proxy_listener_enabled\":false,\"intruder_payload_processor_parameter_name\":\"iss\"}";
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isEqualTo(false);
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(CYAN);
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(PAYLOAD);
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("name");
    }

    @Test
    void givenValidIntruderConfig_butInvalidProxyConfig_whenLoadOrCreateCalled_thenAppropriateConfigReturned() {
        String json = "{\"proxy_history_highlight_color\":\"cyan\",\"intruder_payload_processor_parameter_name\":\"iss\",\"intruder_payload_processor_fuzz_location\":\"header\"}";
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isEqualTo(true);
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(HEADER);
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("iss");
    }

    @Test
    void givenValidIntruderAndProxyConfig_whenLoadOrCreateCalled_thenAppropriateConfigReturned() {
        String json = "{\"proxy_history_highlight_color\":\"cyan\",\"proxy_listener_enabled\":false,\"intruder_payload_processor_parameter_name\":\"iss\",\"intruder_payload_processor_fuzz_location\":\"header\"}";
        BurpConfigPersistence configPersistence = new BurpConfigPersistence(callbacks);
        when(callbacks.getString(BURP_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig()).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isEqualTo(false);
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(CYAN);
        assertThat(burpConfig.intruderConfig()).isNotNull();
        assertThat(burpConfig.intruderConfig().fuzzLocation()).isEqualTo(HEADER);
        assertThat(burpConfig.intruderConfig().fuzzParameter()).isEqualTo("iss");
    }
}