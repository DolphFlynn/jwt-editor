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

package com.blackberry.jwteditor.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static com.blackberry.jwteditor.utils.HexUtils.decodeHex;
import static com.blackberry.jwteditor.utils.HexUtils.encodeHex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class HexUtilsTest {
    private static Stream<Arguments> data() {
        return Stream.of(
                arguments(654177009191L, "00984FFCFE27"),
                arguments(278556392304L, "40DB413B70"),
                arguments(188301794713L, "2BD7A95D99"),
                arguments(797280016540L, "00B9A197989C"),
                arguments(133554316704L, "1F187525A0"),
                arguments(197511780770L, "2DFC9E99A2"),
                arguments(830271757364L, "00C1500D9C34"),
                arguments(751207006520L, "00AEE76D4138"),
                arguments(485989140040L, "712735C648"),
                arguments(773807672239L, "00B42A880BAF"),
                arguments(74636962145L, "1160B5A961"),
                arguments(273099414872L, "3F95FE5D58"),
                arguments(686747610432L, "009FE558ED40"),
                arguments(630301260556L, "0092C0E1FB0C"),
                arguments(624078379017L, "00914DF85809"),
                arguments(744851918895L, "00AD6CA2502F"),
                arguments(588825161216L, "008918B6EE00"),
                arguments(357104952288L, "53251D23E0"),
                arguments(844154888158L, "00C48B8D5FDE"),
                arguments(529842645605L, "7B5D152A65"),
                arguments(970103808314L, "00E1DEB1613A"),
                arguments(872701852936L, "00CB31157D08"),
                arguments(323571739243L, "4B5660EA6B"),
                arguments(776439600113L, "00B4C76813F1"),
                arguments(65419145571L, "0F3B48F163"),
                arguments(70241318753L, "105AB57761"),
                arguments(439394220917L, "664DEFBB75"),
                arguments(399402515249L, "5CFE3EBB31"),
                arguments(190257380364L, "2C4C393C0C"),
                arguments(657736641011L, "00992428A9F3"),
                arguments(671132849677L, "009C42A2960D"),
                arguments(804640743721L, "00BB58536129"),
                arguments(407529000503L, "5EE29F0E37"),
                arguments(797175491321L, "00B99B5CAAF9"),
                arguments(17978978439L, "042FA17087"),
                arguments(509700780985L, "76AC88B3B9"),
                arguments(314841946249L, "494E0AD889"),
                arguments(875093710210L, "00CBBFA65582"),
                arguments(765070969991L, "00B221C88C87"),
                arguments(930522460969L, "00D8A775F329")
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    void givenData_whenConvertedToHex_thenHexCorrect(long number, String expectedHex) {
        byte[] originalData = BigInteger.valueOf(number).toByteArray();

        String actualHex = encodeHex(originalData);

        assertThat(actualHex).isEqualToIgnoringCase(expectedHex);
    }

    @ParameterizedTest
    @MethodSource("data")
    void givenData_whenRoundTripViaHex_thenDataInvariant(long number) {
        byte[] originalData = BigInteger.valueOf(number).toByteArray();

        byte[] roundTrippedData = decodeHex(encodeHex(originalData));

        assertThat(roundTrippedData).isEqualTo(originalData);
    }
}