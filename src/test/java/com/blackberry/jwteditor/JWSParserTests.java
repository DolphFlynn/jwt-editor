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

import com.blackberry.jwteditor.model.jose.JWS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JWSParserTests {
    private static Stream<String> validJws() {
        return Stream.of(
                // JWS
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // JWS without signature
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.",
                // JWS with preceding text
                "asdasdasdeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // JWS without signature preceding text
                "asdasdasdasdeyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // No header
                ".eyJzdWIiOiJUZXN0In0.Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                // No payload
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..Nabf3xakZubPnCzHT-fx0vG1iuNPeJKuSzHxUiQKf-8",
                //URL
                "www.blackberry.com"
        );
    }

    private static Stream<String> invalidJws() {
        return Stream.of(
               ".",
                "..",
                "..."
        );
    }

    @ParameterizedTest
    @MethodSource("validJws")
    void testValidJWS(String joseObjectString) throws ParseException {
        JWS jws = JWS.parse(joseObjectString);

        assertThat(jws.serialize()).isEqualTo(joseObjectString);
    }

    @ParameterizedTest
    @MethodSource("invalidJws")
    void testInvalidJWS(String joseObjectString) {
        assertThrows(ParseException.class, () -> JWS.parse(joseObjectString));
    }
}
