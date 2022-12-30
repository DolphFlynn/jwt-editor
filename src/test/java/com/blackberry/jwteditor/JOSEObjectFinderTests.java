/*
Author : Fraser Winterborn

Copyright 2021 BlackBerry Limited

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

import com.blackberry.jwteditor.model.jose.JWE;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.MutableJOSEObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.blackberry.jwteditor.model.jose.JOSEObjectFinder.containsJOSEObjects;
import static com.blackberry.jwteditor.model.jose.JOSEObjectFinder.extractJOSEObjects;
import static org.assertj.core.api.Assertions.assertThat;

class JOSEObjectFinderTests {
    private static Stream<String> validJws() {
        return Stream.of(
                // JWS
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // JWS with whitespace
                " eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8 ",
                // JWS without signature
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.",
                // JWS with preceding text
                "asdasdasdeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // JWS without signature preceding text
                "asdasdasdasdeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // JWS with empty payload
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyB9.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgfQ.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSH"
        );
    }

    private static Stream<String> invalidJws() {
        return Stream.of(
                "",
                ".",
                "...",
                "asdadasdasdeyJhbGci$iJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                "asdadasdasdeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ^ZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // No header
                ".eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // No payload
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // Empty
                "..",
                //URL
                "www.blackberry.com",
                // Invalid charset encoding
                "K0FIc0FJZy10eXArQUNJOitBQ0ktSldUK0FDSSwrQUNJLWFsZytBQ0k6K0FDSS1IUzI1NitBQ0lBZlEt.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "_v8AewAiAHQAeQBwACIAOgAiAEoAVwBUACIALAAiAGEAbABnACIAOgAiAEgAUwAyADUANgAiAH0.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.K0FIc0FJZy1zdWIrQUNJOitBQ0ktVGVzdCtBQ0lBZlEt.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9._v8AewAiAHMAdQBiACIAOgAiAFQAZQBzAHQAIgB9.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                // Missing or invalid 'alg'
                "eyJ0eXAiOiJKV1QifQ.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiIifQ.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiIgIn0.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiIgICAgIn0.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOm51bGx9.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOjQ0OH0.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOltdfQ.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJ0eXAiOiJKV1QiLCJhbGciOnt9fQ.eyJzdWIiOiJUZXN0In0.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                // Payload not JSON object
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.bnVsbA.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.NDQ4.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.InNhdW5hIg.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSHw",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.W10.17DlZn0zeYhz3uTQCRpSx9hYlUj1SJxDMeZLof8dSH"
        );
    }

    private static Stream<String> validJwe() {
        return Stream.of(
                //JWE with encrypted key
                "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiQTEyOEtXIn0.H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL.qF5-N_7K8VQ4yMSz.WXUNY6eg5fR4tc8Hqf5XDRM9ALGwcQyYG4IYwwg8Ctkx1UuxoV7t6UnemjzCj2sOYUqi3KYpDzrKVJpzokz0vcIem4lFe5N_ds8FAMpW0GSF9ePA8qvV99WaP0N2ECVPmgihvL6qwNhdptlLKtxcOpE41U5LnU22voPK55VF4_1j0WmTgWgZ7DwLDysp6EIDjrrt-DY.febBmP71KADmKRVfeSnv_g",
                //JWE with dir encryption
                "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..FofRkmAUlKShyhYp.1AjXmQsKwV36LSxZ5YJq7xPPTTUS_e9FyLbd-CWdX72ESWMttHm2xGDWUl-Sp9grmcINWLNwsKezYnJVncfir2o9Uq9vcXENIypU2Qwmymn5q5gJwkR4Wx_RLae9Zm8xP76LJFQe8FssUVHx65Zzvd1I6GbV6FjfbkLF1Z_Ka-olubtWCilFDjIVN7WRUAxmV8syJaM.P0XjuL8_8nK50paY09mB6g",
                //JWE with encrypted key with preceding text
                "asdasdasdaseyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiQTEyOEtXIn0.H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL.qF5-N_7K8VQ4yMSz.WXUNY6eg5fR4tc8Hqf5XDRM9ALGwcQyYG4IYwwg8Ctkx1UuxoV7t6UnemjzCj2sOYUqi3KYpDzrKVJpzokz0vcIem4lFe5N_ds8FAMpW0GSF9ePA8qvV99WaP0N2ECVPmgihvL6qwNhdptlLKtxcOpE41U5LnU22voPK55VF4_1j0WmTgWgZ7DwLDysp6EIDjrrt-DY.febBmP71KADmKRVfeSnv_g",
                //JWE with dir encryption with preceding text
                "asdasdasdeyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiZGlyIn0..FofRkmAUlKShyhYp.1AjXmQsKwV36LSxZ5YJq7xPPTTUS_e9FyLbd-CWdX72ESWMttHm2xGDWUl-Sp9grmcINWLNwsKezYnJVncfir2o9Uq9vcXENIypU2Qwmymn5q5gJwkR4Wx_RLae9Zm8xP76LJFQe8FssUVHx65Zzvd1I6GbV6FjfbkLF1Z_Ka-olubtWCilFDjIVN7WRUAxmV8syJaM.P0XjuL8_8nK50paY09mB6g"
        );
    }

    private static Stream<String> invalidJwe() {
        return Stream.of(
                //No header
                ".H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL.qF5-N_7K8VQ4yMSz.WXUNY6eg5fR4tc8Hqf5XDRM9ALGwcQyYG4IYwwg8Ctkx1UuxoV7t6UnemjzCj2sOYUqi3KYpDzrKVJpzokz0vcIem4lFe5N_ds8FAMpW0GSF9ePA8qvV99WaP0N2ECVPmgihvL6qwNhdptlLKtxcOpE41U5LnU22voPK55VF4_1j0WmTgWgZ7DwLDysp6EIDjrrt-DY.febBmP71KADmKRVfeSnv_g",
                //No IV
                "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiQTEyOEtXIn0.H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL..WXUNY6eg5fR4tc8Hqf5XDRM9ALGwcQyYG4IYwwg8Ctkx1UuxoV7t6UnemjzCj2sOYUqi3KYpDzrKVJpzokz0vcIem4lFe5N_ds8FAMpW0GSF9ePA8qvV99WaP0N2ECVPmgihvL6qwNhdptlLKtxcOpE41U5LnU22voPK55VF4_1j0WmTgWgZ7DwLDysp6EIDjrrt-DY.febBmP71KADmKRVfeSnv_g",
                //No Ciphertext
                "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiQTEyOEtXIn0.H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL.qF5-N_7K8VQ4yMSz..febBmP71KADmKRVfeSnv_g",
                //No Tag
                "eyJlbmMiOiJBMTI4R0NNIiwiYWxnIjoiQTEyOEtXIn0.H3X6mT5HLgcFfzLoe4ku6Knhh9Ofv1eL.qF5-N_7K8VQ4yMSz.WXUNY6eg5fR4tc8Hqf5XDRM9ALGwcQyYG4IYwwg8Ctkx1UuxoV7t6UnemjzCj2sOYUqi3KYpDzrKVJpzokz0vcIem4lFe5N_ds8FAMpW0GSF9ePA8qvV99WaP0N2ECVPmgihvL6qwNhdptlLKtxcOpE41U5LnU22voPK55VF4_1j0WmTgWgZ7DwLDysp6EIDjrrt-DY.",
                //Empty
                "....",
                "1.2.www.blackberry.com"
        );
    }

    @ParameterizedTest
    @MethodSource("validJws")
    void testValidJWS(String joseObjectString) {
        List<MutableJOSEObject> joseObjects = extractJOSEObjects(joseObjectString);

        assertThat(joseObjects).hasSize(1);
        assertThat(joseObjects.get(0).getModified()).isInstanceOf(JWS.class);
    }

    @ParameterizedTest
    @MethodSource("validJws")
    void testDetectValidJWS(String joseObjectString) {
        assertThat(containsJOSEObjects(joseObjectString)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidJws")
    void testInvalidJWS(String joseObjectString) {
        List<MutableJOSEObject> joseObjects = extractJOSEObjects(joseObjectString);

        assertThat(joseObjects).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidJws")
    void testDetectInvalidJWS(String joseObjectString) {
        assertThat(containsJOSEObjects(joseObjectString)).isFalse();

    }

    @ParameterizedTest
    @MethodSource("validJwe")
    void testValidJWE(String joseObjectString) {
        List<MutableJOSEObject> joseObjects = extractJOSEObjects(joseObjectString);

        assertThat(joseObjects).hasSize(1);
        assertThat(joseObjects.get(0).getModified()).isInstanceOf(JWE.class);
    }

    @ParameterizedTest
    @MethodSource("validJwe")
    void testDetectValidJWE(String joseObjectString) {
        assertThat(containsJOSEObjects(joseObjectString)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidJwe")
    void testInvalidJWE(String joseObjectString) {
        List<MutableJOSEObject> joseObjects = extractJOSEObjects(joseObjectString);

        assertThat(joseObjects).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidJwe")
    void testDetectInvalidJWE(String joseObjectString) {
        assertThat(containsJOSEObjects(joseObjectString)).isFalse();
    }
}
