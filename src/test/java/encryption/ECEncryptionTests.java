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
import com.blackberry.jwteditor.model.keys.JWKKeyFactory;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import utils.BouncyCastleExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.blackberry.jwteditor.utils.PEMUtils.pemToECKey;
import static com.nimbusds.jose.EncryptionMethod.*;
import static com.nimbusds.jose.JWEAlgorithm.*;
import static data.PemData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static utils.ArgumentUtils.cartesianProduct;

@ExtendWith(BouncyCastleExtension.class)
class ECEncryptionTests {
    private static final String TEST_JWS = "eyJhbGciOiJFUzM4NCIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJDYXJsb3MifQ.6g2LVJpzWxC4NsVk2IFNZ-ZaaSbbnto0rKVQWZvjRsQZUfJSOM0NO1mmunW9wtNHdlkQr-jTwx-O310EoVP0bRBjf1Vd8io2loupt-8CUeVKWqMJkVp16bzDDlURSpVt";
    private static final List<JWEAlgorithm> ENCRYPTION_ALGORITHMS = List.of(ECDH_ES, ECDH_ES_A128KW, ECDH_ES_A192KW, ECDH_ES_A256KW);
    private static final List<EncryptionMethod> ENCRYPTION_METHODS = List.of(
            A128GCM,
            A192GCM,
            A256GCM,
            A128CBC_HS256,
            A192CBC_HS384,
            A256CBC_HS512
    );

    private static Stream<Arguments> canEncryptWithEC() {
        return Stream.of(
                arguments(PRIME256v1Public, true),
                arguments(PRIME256v1PrivateSEC1, true),
                arguments(PRIME256v1PrivatePKCS8, true),
                arguments(SECP256K1Public, false),
                arguments(SECP256K1PrivateSEC1, false),
                arguments(SECP256K1PrivatePKCS8, false),
                arguments(SECP384R1Public, true),
                arguments(SECP384R1PrivateSEC1, true),
                arguments(SECP384R1PrivatePKCS8, true),
                arguments(SECP521R1PrivateSEC1, true),
                arguments(SECP521R1PrivatePCKS8, true),
                arguments(SECP521R1Public, true)
        );
    }

    @ParameterizedTest
    @MethodSource("canEncryptWithEC")
    void canEncryptWithAllECKeysExceptThoseUsingSecp256k1(String pem, boolean canEncrypt) throws Exception {
        JWK ecKey = pemToECKey(pem);
        JWKKey key = JWKKeyFactory.from(ecKey);
        assertThat(key.canEncrypt()).isEqualTo(canEncrypt);
    }

    private static Stream<Arguments> canDecryptWithEC() {
        return Stream.of(
                arguments(PRIME256v1PrivateSEC1, true),
                arguments(PRIME256v1PrivatePKCS8, true),
                arguments(SECP256K1PrivateSEC1, false),
                arguments(SECP256K1PrivatePKCS8, false),
                arguments(SECP384R1PrivateSEC1, true),
                arguments(SECP384R1PrivatePKCS8, true),
                arguments(SECP521R1PrivateSEC1, true),
                arguments(SECP521R1PrivatePCKS8, true),
                arguments(PRIME256v1Public, false),
                arguments(SECP256K1Public, false),
                arguments(SECP384R1Public, false),
                arguments(SECP521R1Public, false)
        );
    }

    @ParameterizedTest
    @MethodSource("canDecryptWithEC")
    void canOnlyDecryptWithPrivateECKeysAndKeysNotUsingSecp256k1(String pem, boolean canDecrypt) throws Exception {
        JWK ecKey = pemToECKey(pem);
        JWKKey key = JWKKeyFactory.from(ecKey);
        assertThat(key.canDecrypt()).isEqualTo(canDecrypt);
    }

    private static Stream<String> ecKeys() {
        return Stream.of(
                PRIME256v1PrivateSEC1,
                PRIME256v1PrivatePKCS8,
                SECP256K1PrivateSEC1,
                SECP256K1PrivatePKCS8,
                SECP384R1PrivateSEC1,
                SECP384R1PrivatePKCS8,
                SECP521R1PrivateSEC1,
                SECP521R1PrivatePCKS8,
                PRIME256v1Public,
                SECP256K1Public,
                SECP384R1Public,
                SECP521R1Public
        );
    }

    @ParameterizedTest
    @MethodSource("ecKeys")
    void ecKeysHaveCorrectEncryptionAlgorithms(String pem) throws Exception {
        JWK ecKey = pemToECKey(pem);
        JWKKey key = JWKKeyFactory.from(ecKey);

        JWEAlgorithm[] keyEncryptionKeyAlgorithms = key.getKeyEncryptionKeyAlgorithms();

        assertThat(keyEncryptionKeyAlgorithms).containsExactly(ECDH_ES, ECDH_ES_A128KW, ECDH_ES_A192KW, ECDH_ES_A256KW);
    }

    private static Stream<Arguments> ecKeysAndAlgorithms() {
        return cartesianProduct(ecKeys().toList(), ENCRYPTION_ALGORITHMS);
    }

    @ParameterizedTest
    @MethodSource("ecKeysAndAlgorithms")
    void ecKeysHaveCorrectEncryptionMethods(String pem, JWEAlgorithm keyEncryptionKeyAlgorithm) throws Exception {
        JWK ecKey = pemToECKey(pem);
        JWKKey key = JWKKeyFactory.from(ecKey);

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

    private static Stream<Arguments> ecKeyPairsAndAlgorithms() throws Exception {
        List<Pair<JWK, JWK>> keyPairs = List.of(
                Pair.of(pemToECKey(PRIME256v1Public), pemToECKey(PRIME256v1PrivateSEC1)),
                Pair.of(pemToECKey(PRIME256v1Public), pemToECKey(PRIME256v1PrivatePKCS8)),
                Pair.of(pemToECKey(SECP384R1Public), pemToECKey(SECP384R1PrivateSEC1)),
                Pair.of(pemToECKey(SECP384R1Public), pemToECKey(SECP384R1PrivatePKCS8)),
                Pair.of(pemToECKey(SECP521R1Public), pemToECKey(SECP521R1PrivateSEC1)),
                Pair.of(pemToECKey(SECP521R1Public), pemToECKey(SECP521R1PrivatePCKS8))
        );

        return cartesianProduct(keyPairs, ENCRYPTION_ALGORITHMS, ENCRYPTION_METHODS);
    }

    @ParameterizedTest
    @MethodSource("ecKeyPairsAndAlgorithms")
    void ecEncryptionConsistency(Pair<JWK, JWK> keyPair, JWEAlgorithm kek, EncryptionMethod cek) throws Exception {
        JWKKey publicKey = JWKKeyFactory.from(keyPair.getLeft());
        JWKKey privateKey = JWKKeyFactory.from(keyPair.getRight());

        JWE jwe = JWEFactory.encrypt(JWSFactory.parse(TEST_JWS), publicKey, kek, cek);
        JWS decrypt = jwe.decrypt(privateKey);

        assertThat(decrypt.serialize()).isEqualTo(TEST_JWS);
    }
}
