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

package burp.intruder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FuzzLocationTest {

    private static Stream<Arguments> validLocationsData() {
        return Stream.of(
                arguments("header", FuzzLocation.HEADER),
                arguments("HEADER", FuzzLocation.HEADER),
                arguments("Header", FuzzLocation.HEADER),
                arguments("payload", FuzzLocation.PAYLOAD),
                arguments("PAYLOAD", FuzzLocation.PAYLOAD),
                arguments("Payload", FuzzLocation.PAYLOAD)
        );
    }

    @ParameterizedTest
    @MethodSource("validLocationsData")
    void testConstructionOfFuzzLocationWithValidLocation(String locationString, FuzzLocation expectedLocation) {
        FuzzLocation fuzzLocation = FuzzLocation.from(locationString);

        assertThat(fuzzLocation).isEqualTo(expectedLocation);
    }

    @Test
    void testConstructionOfFuzzLocationWithInvalidLocation() {
        FuzzLocation fuzzLocation = FuzzLocation.from("signature");

        assertThat(fuzzLocation).isNull();
    }
}