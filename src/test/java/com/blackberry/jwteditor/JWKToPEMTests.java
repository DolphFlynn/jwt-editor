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

import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.PasswordKey;
import com.blackberry.jwteditor.utils.PEMUtils;
import com.blackberry.jwteditor.utils.PEMUtils.PemException;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JWKToPEMTests {
    static final String RSA2048PrivateDEN = "{\"kty\": \"RSA\", \"d\": \"NWk0zSEQGWDeAuuVa_5N_oNr5K2whMhvbCPFHjUPFpfcA3FMAPMHT17u9GMBlum2pgYrvRHsibfn0i5pRxpFnSEvqnzYubWlaVSiRL1xTLjn6UixpxwfJkPY8C1dBa9aPZvSO_R-MfHoDudVecckMrWWdScw_9nir_Fc2l8QCp4R_OYGu6Uj58YDYCrToJ817utPltDRaTBg_7FD-ppX9-qdoBB1RW5kzJqGksBt4gk6zBeagvryvCfxVtrkYVv4cVJ8K1i2waMXRtAl-cyNTJh4M6zcsBM8pO6DirxBQdmzYsz_20w3vPdPs3yH53XxbAMXTEwlZz3lrJaoFAi6eQ\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";
    static final String RSA2048PrivateAll = "{\"p\": \"4lzEStkwPU75h1uGr8Tn2hYAouFLhR8nnN7XV66UM6wQkJwA6pBE1FDb-xRvmZyrz9oGeryYrs6cE34tfYVkZYjyO95sE4ABi44986RmioHMg1-P5Ip8iq7_CMagq1WGgyi5Bx-hRtOxhnGZT3W2Juz34VWelDRsRNnbV0-ZV5c\", \"kty\": \"RSA\", \"q\": \"ryP4Uo9wMeA_nvGHsLwOjTaFWm0aLYZDm06U7i8Tl3y1NO1zgez2qyuGyPA0RYPrKVdYU9275RRv7hT1I2SxEr5RT6IXFOoXZOtrtocmLll2W2cVVZqfsHoVtD_TcZ4haMqp8hUOVW0i_nc4uSMPQFLSVgfLaRLXi80jcFpLwB0\", \"d\": \"NWk0zSEQGWDeAuuVa_5N_oNr5K2whMhvbCPFHjUPFpfcA3FMAPMHT17u9GMBlum2pgYrvRHsibfn0i5pRxpFnSEvqnzYubWlaVSiRL1xTLjn6UixpxwfJkPY8C1dBa9aPZvSO_R-MfHoDudVecckMrWWdScw_9nir_Fc2l8QCp4R_OYGu6Uj58YDYCrToJ817utPltDRaTBg_7FD-ppX9-qdoBB1RW5kzJqGksBt4gk6zBeagvryvCfxVtrkYVv4cVJ8K1i2waMXRtAl-cyNTJh4M6zcsBM8pO6DirxBQdmzYsz_20w3vPdPs3yH53XxbAMXTEwlZz3lrJaoFAi6eQ\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"qi\": \"hWYpdilJqiGG7lMEdEPEjPuwycq21YXexob2WTbCxJcF3Yy0vC_zSzxPB1qExA18-Zz9lUwnI9F21gBxvCactWZJ9tvwwawfTbX_9OqOl_ebORFvWKEsoFfpVCQXhwHCTB-kf9ShmDzxBnEuTcok5EXZi6xrofM3Y0PZotSgSoo\",\"dp\": \"h4Xsy7cup3YR9RU6FR_5g9tqdBoYwdG-QLA2EzvlZO5eWIXeEpFfdBIZMkCw9DIVt3KcMH2bmAUA8ra3e5ASZKvSA0AOSrp3slruAmHqNoCxtfHPz4-OMuXEsTdiWFHzH7GQ3Y_1WddCUPDQTf92l-WGHvXI5IhiTfJ03Ng-QW8\", \"dq\": \"m7RG2F9dR3ouFYh1MdJ-vVxzQektFLwA7tn13atMp6jfEKbpweCBi7uuoIWscwDM2Hwmsqi2mvqIaAmJxmWGZzt73mgkTRuwoLALmsKcVyiB6NDETs6gmaxwD0ePG7uRyDAk1muRyrC0I7aqXy2kKXN4O7PCSy_NISTHFOOx5KE\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";
    static final String RSA2048Public = "{\"kty\": \"RSA\", \"e\": \"AQAB\", \"kid\": \"4c756ccb-0080-42d3-bc58-7692fd2ab3c5\", \"n\": \"mt04cN9oFDdmSDWkuxnsxyA6FJvEi1Ha1bYOUNLnS1Ecoqe7ErvDoycctBrAZ2ul408zFSoSRqf7IX5P3W2b8KDh7QvToZ0o9E0DBFWlqo88Iariif4ZpXL7r2Kse67zZ7ZVmU4A3aB_8mbpZQutQEWwuhTANnzc94pDv6ryaqcAc4rPgsnE1IY2gqznWBL2ZGa2iCzphTeB4yxSr3AfLY8HOUSku4Vvr6l20t-_ZbaSJFDqvjo699w9LkqzzxNTtdOmenxRc8eEKg6_Cw_Do_Q-SgblDXMWI057u-pSXbYjbknk20HczULLxgS7nUQJhs8uN8tX-xcQuoZexU0sGw\"}";

    static final String P256Private = "{\"kty\": \"EC\", \"d\": \"UBcNQgxNE1d7JdTe6G8ez5TtM4c6qSHmgKtiIZHGq18\", \"crv\": \"P-256\", \"kid\": \"c572f6eb-a48b-4b2b-bfe2-dd6722e6d0c6\", \"x\": \"4OO4UgLJ6L0XdENG6T7R6VRj31Zq9ecRwb8eKuensns\", \"y\": \"eKBa5Q-_ClY848UpH90G94ve54m3JSx6dLyJIUArwsA\"}";
    static final String P256Public = "{\"kty\": \"EC\", \"crv\": \"P-256\", \"kid\": \"c572f6eb-a48b-4b2b-bfe2-dd6722e6d0c6\", \"x\": \"4OO4UgLJ6L0XdENG6T7R6VRj31Zq9ecRwb8eKuensns\", \"y\": \"eKBa5Q-_ClY848UpH90G94ve54m3JSx6dLyJIUArwsA\"}";

    static final String SECP256K1Private = "{\"kty\": \"EC\", \"d\": \"CBT5lhTynh7L8Z1Uh6wXwXsxx-ho8-aggmXq5Qw6iAM\", \"crv\": \"secp256k1\", \"kid\": \"a6d1ba27-45be-4ec2-9324-58ffe764b898\", \"x\": \"ECR1kI36fUH5Xt2RfSCS0XW1Qhc9pD2hnn7HvHvLfr0\", \"y\": \"z6OUuRROlya9PyOieGzkPcajR0og_95i8A9NdrxVk4A\"}";
    static final String SECP256K1Public = "{\"kty\": \"EC\", \"crv\": \"secp256k1\", \"kid\": \"a6d1ba27-45be-4ec2-9324-58ffe764b898\", \"x\": \"ECR1kI36fUH5Xt2RfSCS0XW1Qhc9pD2hnn7HvHvLfr0\", \"y\": \"z6OUuRROlya9PyOieGzkPcajR0og_95i8A9NdrxVk4A\"}";

    static final String P384Private = "{\"kty\": \"EC\", \"d\": \"EI2fP6ghM00yeXhkgMp7Lk8bUfdw2hKM5qLZtlBE754Lmrw2a0eKKCAwVYnbRrGC\", \"crv\": \"P-384\", \"kid\": \"78f3da69-7aa9-4ef5-9e25-3b07ea34e8cc\", \"x\": \"jTbrGKUR28MzNoQjE1PE-K-iD0pDRwkvnGtbrQxIKN0oXU7HwpgcSBezxmCRsSqq\", \"y\": \"c6PlDhTHnpsQU1jakL35blrpsWPlQhAA-nMOdyzlIwYS899NC-60xFBLGu3YHQsf\"}";
    static final String P384Public = "{\"kty\": \"EC\", \"crv\": \"P-384\", \"kid\": \"78f3da69-7aa9-4ef5-9e25-3b07ea34e8cc\", \"x\": \"jTbrGKUR28MzNoQjE1PE-K-iD0pDRwkvnGtbrQxIKN0oXU7HwpgcSBezxmCRsSqq\", \"y\": \"c6PlDhTHnpsQU1jakL35blrpsWPlQhAA-nMOdyzlIwYS899NC-60xFBLGu3YHQsf\"}";

    static final String P521Private = "{\"kty\": \"EC\", \"d\": \"AEAmcxfuAJoT38ig1QU2APZm0QDtfJMyB_lvgb9lfsdvAMI5peD2BfPz2U_S1-h4R1w-EINd1tdEsz8PsqbJ95tx\", \"crv\": \"P-521\", \"kid\": \"635cf969-e377-4034-895f-1a6022a0e93d\", \"x\": \"Afp3fCwa4qZtG0DD1_CZJMrlOcuUmPH2MWd1b5gL8xSDsZIDURE_4KB3b0gihHN6MJavyRb8BwofVyPds4aa2e0A\", \"y\": \"ATTwJtHUTfIFVDpyMAtZeQ2xdXFohr_ETegtn1WGf9gmkrHvCZbbTYyU9el9IGA6A8FnSHAAN3imkOKkDIbEcRwn\"}";
    static final String P521Public = "{\"kty\": \"EC\", \"crv\": \"P-521\", \"kid\": \"635cf969-e377-4034-895f-1a6022a0e93d\", \"x\": \"Afp3fCwa4qZtG0DD1_CZJMrlOcuUmPH2MWd1b5gL8xSDsZIDURE_4KB3b0gihHN6MJavyRb8BwofVyPds4aa2e0A\", \"y\": \"ATTwJtHUTfIFVDpyMAtZeQ2xdXFohr_ETegtn1WGf9gmkrHvCZbbTYyU9el9IGA6A8FnSHAAN3imkOKkDIbEcRwn\"}";

    static final String X25519Private = "{\"kty\": \"OKP\", \"d\": \"t75j8DATl-aDTKOlxMJsBt-P1Q4Lyy4SRQXyQ2yEgo4\", \"crv\": \"X25519\", \"kid\": \"4778a36f-1cf4-45c2-a138-9fa1151346e9\", \"x\": \"r8UOGY16orlHR9BZcwf86-mx35XeAOdDZ-_ZA2AoKT0\"}";
    static final String X25519Public = "{\"kty\": \"OKP\", \"crv\": \"X25519\", \"kid\": \"4778a36f-1cf4-45c2-a138-9fa1151346e9\", \"x\": \"r8UOGY16orlHR9BZcwf86-mx35XeAOdDZ-_ZA2AoKT0\"}";

    static final String Ed25519Private = "{\"kty\": \"OKP\", \"d\": \"_1iBm2slEqE8ekqBTX_Hx1-3qU_zy50H4rppWJ2e6dg\", \"crv\": \"Ed25519\", \"kid\": \"484a783c-0480-4198-97bc-b7086e44bcbd\", \"x\": \"aLGXCU3U-zrgk0o6IA0kH01nmJitziqzNpdZWqVr6GQ\"}";
    static final String Ed25519Public = "{\"kty\": \"OKP\", \"crv\": \"Ed25519\", \"kid\": \"484a783c-0480-4198-97bc-b7086e44bcbd\", \"x\": \"aLGXCU3U-zrgk0o6IA0kH01nmJitziqzNpdZWqVr6GQ\"}";

    static final String[] RSAPrivate = {
            RSA2048PrivateAll,
            //RSA2048PrivateDEN
    };

    static final String[] RSAPublic = {
            RSA2048Public
    };

    static final String[] ECPrivate = {
            P256Private,
            SECP256K1Private,
            P384Private,
            P521Private
    };

    static final String[] ECPublic = {
            P256Public,
            SECP256K1Public,
            P384Public,
            P521Public
    };

    static final String[] OKPPrivate = {
            X25519Private,
            Ed25519Private
    };

    static final String[] OKPPublic = {
            X25519Public,
            Ed25519Public
    };

    private static Stream<String> ecPrivateValidJWKs() {
        return Stream.of(ECPrivate);
    }

    @ParameterizedTest
    @MethodSource("ecPrivateValidJWKs")
    void ecPrivateKeyToPemValid(String jwkString) throws PemException, ParseException {
        ECKey ecKey = ECKey.parse(jwkString);
        assertThat(ecKey.isPrivate()).isTrue();

        String pem = PEMUtils.jwkToPem(ecKey);
        assertThat(pem).isNotEmpty();
    }

    private static Stream<String> ecPublicValidJWKs() {
        return Stream.of(ECPublic);
    }

    @ParameterizedTest
    @MethodSource("ecPublicValidJWKs")
    void ecPublicKeyToPemValid(String jwkString) throws PemException, ParseException {
        ECKey ecKey = ECKey.parse(jwkString);
        assertThat(ecKey.isPrivate()).isFalse();

        String pem = PEMUtils.jwkToPem(ecKey);
        assertThat(pem).isNotEmpty();
    }

    private static Stream<String> ecInvalidJWKs() {
        return Stream.of(RSAPublic, RSAPrivate, OKPPrivate, OKPPublic).flatMap(Arrays::stream);
    }

    @ParameterizedTest
    @MethodSource("ecInvalidJWKs")
    void ecKeyToPemInvalid(String jwkString) {
        assertThrows(Exception.class, () -> ECKey.parse(jwkString));
    }

    static Stream<String> rsaPrivateValidJWKs() {
        return Stream.of(RSAPrivate);
    }

    @ParameterizedTest
    @MethodSource("rsaPrivateValidJWKs")
    void rsaPrivateKeyToPemValid(String jwkString) throws PemException, ParseException {
        RSAKey rsaKey = RSAKey.parse(jwkString);
        assertThat(rsaKey.isPrivate()).isTrue();

        String pem = PEMUtils.jwkToPem(rsaKey);
        assertThat(pem).isNotEmpty();
    }

    static Stream<String> rsaPublicValidJWKs() {
        return Stream.of(RSAPublic);
    }

    @ParameterizedTest
    @MethodSource("rsaPublicValidJWKs")
    void rsaPublicKeyToPemValid(String jwkString) throws PemException, ParseException {
        RSAKey rsaKey = RSAKey.parse(jwkString);
        assertThat(rsaKey.isPrivate()).isFalse();

        String pem = PEMUtils.jwkToPem(rsaKey);
        assertThat(pem).isNotEmpty();
    }

    private static Stream<String> rsaInvalidJWKs() {
        return Stream.of(ECPrivate, ECPublic, OKPPrivate, OKPPublic).flatMap(Arrays::stream);
    }

    @ParameterizedTest
    @MethodSource("rsaInvalidJWKs")
    void rsaKeyToPemInvalid(String jwkString) {
        assertThrows(Exception.class, () -> RSAKey.parse(jwkString));
    }

    private static Stream<String> ocpPrivateValidJWKs() {
        return Stream.of(OKPPrivate);
    }

    @ParameterizedTest
    @MethodSource("ocpPrivateValidJWKs")
    void octetKeyPairPrivateToPemValid(String jwkString) throws PemException, ParseException {
        OctetKeyPair octetKeyPair = OctetKeyPair.parse(jwkString);
        assertThat(octetKeyPair.isPrivate()).isTrue();

        String pem = PEMUtils.jwkToPem(octetKeyPair);
        assertThat(pem).isNotEmpty();
    }

    private static Stream<String> ocpPublicValidJWKs() {
        return Stream.of(OKPPublic);
    }

    @ParameterizedTest
    @MethodSource("ocpPublicValidJWKs")
    void octetKeyPairPublicToPemValid(String jwkString) throws PemException, ParseException {
        OctetKeyPair octetKeyPair = OctetKeyPair.parse(jwkString);
        assertThat(octetKeyPair.isPrivate()).isFalse();

        String pem = PEMUtils.jwkToPem(octetKeyPair);
        assertThat(pem).isNotEmpty();
    }

    private static Stream<String> ocpInvalidJWKs() {
        return Stream.of(ECPrivate, ECPublic, RSAPrivate, RSAPublic).flatMap(Arrays::stream);
    }

    @ParameterizedTest
    @MethodSource("ocpInvalidJWKs")
    void octetKeyPairToPemInvalid(String jwkString) {
        assertThrows(Exception.class, () -> OctetKeyPair.parse(jwkString));
    }

    private static Stream<Arguments> keyToPemData() throws ParseException, Key.UnsupportedKeyException {
        return Stream.of(
                arguments(new JWKKey(RSAKey.parse(RSA2048PrivateDEN)), true),
                arguments(new JWKKey(ECKey.parse(P256Private)), true),
                arguments(new JWKKey(OctetKeyPair.parse(Ed25519Private)), true),
                arguments(new JWKKey(new OctetSequenceKey.Builder("secret123".getBytes()).build()), false),
                arguments(new PasswordKey("keyId", "secret123", 17, 29), false)
        );
    }

    @ParameterizedTest
    @MethodSource("keyToPemData")
    void testCanConvertToPemForJWKs(Key key, boolean canConvert) {
        assertThat(key.canConvertToPem()).isEqualTo(canConvert);
    }
}
