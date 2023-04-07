/*
Author : Dolph Flynn

Copyright 2023 Dolph Flynn

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

package burp.api.montoya.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FakeByteArrayTest {
    private final ByteArray byteArray = new FakeByteArray("0123456789abcedf");

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                arguments(-1, 0),
                arguments(0, 0),
                arguments(1, 1),
                arguments(0, -1),
                arguments(2, 1),
                arguments(0, 17)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidParameters")
    void givenInvalidParameters_whenSubArrayInvoked_thenExceptionThrown(int start, int end) {
        assertThrows(IllegalArgumentException.class, () -> byteArray.subArray(start, end));
    }

    static Stream<Arguments> validData() {
        return Stream.of(
                arguments(0, 1, "0"),
                arguments(0, 16, "0123456789abcedf"),
                arguments(0, 7, "0123456"),
                arguments(7, 16, "789abcedf"),
                arguments(15, 16, "f")
        );
    }

    @ParameterizedTest
    @MethodSource("validData")
    void givenValidParameters_whenSubArrayInvoked_thenExpectedDataReturned(int start, int end, String expected) {
        ByteArray bytes = byteArray.subArray(start, end);

        assertThat(bytes.toString()).isEqualTo(expected);
    }
}