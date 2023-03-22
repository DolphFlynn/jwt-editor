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

package com.blackberry.jwteditor.model.jose;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetKeyPair;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;

import static com.blackberry.jwteditor.JWKData.Ed25519Public;
import static com.blackberry.jwteditor.JWKData.SECP256K1Public;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class JWKSetTest {

    @Test
    void givenNoKeys_whenJWKSetSerialized_thenKeysEmpty() {
        List<JWK> jwkList = emptyList();
        JWKSet jwkSet = new JWKSet(jwkList);

        String json = jwkSet.serialize();

        assertThat(json).isEqualToIgnoringWhitespace("{\"keys\":[]}");
    }

    @Test
    void givenSingleKey_whenJWKSetSerialized_thenKeysHasSingleEntry() throws ParseException  {
        List<JWK> jwkList =List.of(ECKey.parse(SECP256K1Public));
        JWKSet jwkSet = new JWKSet(jwkList);

        String json = jwkSet.serialize();

        assertThat(json).isEqualToIgnoringWhitespace("{\"keys\":[%s]}".formatted(SECP256K1Public));
    }

    @Test
    void givenMultipleKey_whenJWKSetSerialized_thenKeysHasMultipleEntry() throws ParseException {
        List<JWK> jwkList =List.of(ECKey.parse(SECP256K1Public), OctetKeyPair.parse(Ed25519Public));
        JWKSet jwkSet = new JWKSet(jwkList);

        String json = jwkSet.serialize();

        assertThat(json).isEqualToIgnoringWhitespace("{\"keys\":[%s, %s]}".formatted(SECP256K1Public, Ed25519Public));
    }
}