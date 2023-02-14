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

package encryption;

import com.blackberry.jwteditor.model.jose.JWE;
import com.blackberry.jwteditor.model.jose.JWEFactory;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.ArgumentUtils;
import utils.BouncyCastleExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.blackberry.jwteditor.utils.PEMUtils.pemToOctetKeyPair;
import static com.nimbusds.jose.EncryptionMethod.*;
import static com.nimbusds.jose.JWEAlgorithm.ECDH_ES;
import static data.PemData.*;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(BouncyCastleExtension.class)
class OKPEncryptionTests {
    private static final String TEST_JWS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJMaWdodG1hbiJ9.cB5NQREgEHk47Zx5XRGWZ85eJ8SgmvBuKW5_Pm4Zo6o";
    private static final List<JWEAlgorithm> ENCRYPTION_ALGORITHMS = singletonList(ECDH_ES);
    private static final List<EncryptionMethod> ENCRYPTION_METHODS = List.of(
            A128CBC_HS256,
            A128GCM,
            A192CBC_HS384,
            A192GCM,
            A256CBC_HS512,
            A256GCM
    );

    private static Stream<Arguments> canEncryptWithOKP() {
        return Stream.of(
                arguments(ED448Public, false),
                arguments(ED25519Public, false),
                arguments(X448Public, true),
                arguments(X25519Public, true),
                arguments(ED448Private, false),
                arguments(ED25519Private, false),
                arguments(X448Private, true),
                arguments(X25519Private, true)
        );
    }

    @ParameterizedTest
    @MethodSource("canEncryptWithOKP")
    void canOnlyEncryptWithOKPKeysOnCurvesX25519AndX448(String pem, boolean canEncrypt) throws Exception {
        JWK okpKey = pemToOctetKeyPair(pem);
        JWKKey key = new JWKKey(okpKey);
        assertThat(key.canEncrypt()).isEqualTo(canEncrypt);
    }

    private static Stream<Arguments> canDecryptWithOKP() {
        return Stream.of(
                arguments(ED448Public, false),
                arguments(ED25519Public, false),
                arguments(X448Public, false),
                arguments(X25519Public, false),
                arguments(ED448Private, false),
                arguments(ED25519Private, false),
                arguments(X448Private, true),
                arguments(X25519Private, true)
        );
    }

    @ParameterizedTest
    @MethodSource("canDecryptWithOKP")
    void canOnlyDecryptWithPrivateOKPKeysOnCurvesX25519AndX448(String pem, boolean canDecrypt) throws Exception {
        JWK okpKey = pemToOctetKeyPair(pem);
        JWKKey key = new JWKKey(okpKey);
        assertThat(key.canDecrypt()).isEqualTo(canDecrypt);
    }

    private static Stream<String> okpKeys() {
        return Stream.of(
                ED448Public,
                ED25519Public,
                X448Public,
                X25519Public,
                ED448Private,
                ED25519Private,
                X448Private,
                X25519Private
        );
    }

    @ParameterizedTest
    @MethodSource("okpKeys")
    void okpKeysHaveCorrectEncryptionAlgorithms(String pem) throws Exception {
        JWK okpKeys = pemToOctetKeyPair(pem);
        JWKKey key = new JWKKey(okpKeys);

        JWEAlgorithm[] keyEncryptionKeyAlgorithms = key.getKeyEncryptionKeyAlgorithms();

        assertThat(keyEncryptionKeyAlgorithms).containsExactly(ECDH_ES);
    }

    private static Stream<Arguments> okpKeysAndAlgorithms() {
        return ArgumentUtils.cartesianProduct(okpKeys().toList(), ENCRYPTION_ALGORITHMS);
    }

    @ParameterizedTest
    @MethodSource("okpKeysAndAlgorithms")
    void okpKeysHaveCorrectEncryptionMethods(String pem, JWEAlgorithm keyEncryptionKeyAlgorithm) throws Exception {
        JWK okpKey = pemToOctetKeyPair(pem);
        JWKKey key = new JWKKey(okpKey);

        EncryptionMethod[] keyEncryptionKeyAlgorithms = key.getContentEncryptionKeyAlgorithms(keyEncryptionKeyAlgorithm);

        assertThat(keyEncryptionKeyAlgorithms).containsExactly(
                A128GCM,
                A192GCM,
                A256GCM,
                A128CBC_HS256,
                A192CBC_HS384,
                A256CBC_HS512
        );
    }

    private static Stream<Arguments> okpKeyPairsAndAlgorithms() throws Exception {
        List<Pair<JWK, JWK>> keyPairs = List.of(
                Pair.of(pemToOctetKeyPair(X448Public), pemToOctetKeyPair(X448Private)),
                Pair.of(pemToOctetKeyPair(X25519Public), pemToOctetKeyPair(X25519Private))
        );

        return ArgumentUtils.cartesianProduct(keyPairs, ENCRYPTION_ALGORITHMS, ENCRYPTION_METHODS);
    }

    @ParameterizedTest
    @MethodSource("okpKeyPairsAndAlgorithms")
    void okpEncryptionConsistency(Pair<JWK, JWK> keyPair, JWEAlgorithm kek, EncryptionMethod cek) throws Exception {
        JWKKey publicKey = new JWKKey(keyPair.getLeft());
        JWKKey privateKey = new JWKKey(keyPair.getRight());

        JWE jwe = JWEFactory.encrypt(JWSFactory.parse(TEST_JWS), publicKey, kek, cek);
        JWS decrypt = jwe.decrypt(privateKey);

        assertThat(decrypt.serialize()).isEqualTo(TEST_JWS);
    }
}
