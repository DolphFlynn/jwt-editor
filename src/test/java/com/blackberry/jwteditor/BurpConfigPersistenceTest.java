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

package com.blackberry.jwteditor;

import burp.api.montoya.persistence.Preferences;
import com.blackberry.jwteditor.model.config.BurpConfig;
import com.blackberry.jwteditor.model.config.HighlightColor;
import com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.blackberry.jwteditor.model.config.ProxyConfig.DEFAULT_HIGHLIGHT_COLOR;
import static com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence.PROXY_LISTENER_SETTINGS_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BurpConfigPersistenceTest {
    private final Preferences callbacks = mock(Preferences.class);

    @Test
    void givenNoSavedConfig_whenLoadOrCreateCalled_thenDefaultInstanceReturned() {
        ProxyConfigPersistence configPersistence = new ProxyConfigPersistence(callbacks);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isTrue();
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
        configPersistence.save(burpConfig);
    }

    private static Stream<String> invalidProxyConfigJson() {
        return Stream.of(
                "",
                "{",
                "]",
                "true",
                "[]",
                "null",
                "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":[]}",
                "{\"proxy_history_highlight_color\":\"red\"}",
                "{\"proxy_listener_enabled\":true}",
                "{\"proxy_history_highlight_color\":\"gunmetal\",\"proxy_listener_enabled\":true}",
                "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":\"turnip\"}"
        );
    }

    @ParameterizedTest
    @MethodSource("invalidProxyConfigJson")
    void givenInvalidSavedConfig_whenLoadOrCreateCalled_thenDefaultInstanceReturned(String invalidJson) {
        ProxyConfigPersistence configPersistence = new ProxyConfigPersistence(callbacks);
        when(callbacks.getString(PROXY_LISTENER_SETTINGS_NAME)).thenReturn(invalidJson);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isTrue();
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(DEFAULT_HIGHLIGHT_COLOR);
    }

    private static Stream<Arguments> validProxyConfigJson() {
        return Stream.of(
                arguments(
                        "{\"proxy_history_highlight_color\":\"red\",\"proxy_listener_enabled\":true}",
                        true,
                        HighlightColor.RED
                ),
                arguments(
                        "{\"proxy_history_highlight_color\":\"cyan\",\"proxy_listener_enabled\":false}",
                        false,
                        HighlightColor.CYAN
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validProxyConfigJson")
    void givenValidSavedConfig_whenLoadOrCreateCalled_thenAppropriateInstanceReturned(String json, boolean listenerEnabled, HighlightColor highlightColor) {
        ProxyConfigPersistence configPersistence = new ProxyConfigPersistence(callbacks);
        when(callbacks.getString(PROXY_LISTENER_SETTINGS_NAME)).thenReturn(json);

        BurpConfig burpConfig = configPersistence.loadOrCreateNew();

        assertThat(burpConfig).isNotNull();
        assertThat(burpConfig.proxyConfig().highlightJWT()).isEqualTo(listenerEnabled);
        assertThat(burpConfig.proxyConfig().highlightColor()).isEqualTo(highlightColor);
    }
}