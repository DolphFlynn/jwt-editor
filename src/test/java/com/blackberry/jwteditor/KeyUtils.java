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

import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.JWKKeyFactory;
import com.blackberry.jwteditor.utils.PEMUtils;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.nimbusds.jose.JWSAlgorithm.*;
import static data.PemData.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class KeyUtils {
    private KeyUtils() {
    }

    static JWKKey rsaKeyFrom(String pem) throws Exception {
        JWK rsaKey = PEMUtils.pemToRSAKey(pem);
        return JWKKeyFactory.from(rsaKey);
    }

    static JWKKey ecKeyFrom(String pem) throws Exception {
        JWK ecKey = PEMUtils.pemToECKey(pem);
        return JWKKeyFactory.from(ecKey);
    }

    static JWKKey okpPrivateKeyFrom(String pem) throws Exception {
        JWK octetKeyPair = PEMUtils.pemToOctetKeyPair(pem);
        return JWKKeyFactory.from(octetKeyPair);
    }

    static Stream<Arguments> keySigningAlgorithmPairs() throws Exception {
        return Stream.of(
                // 512-bit RSA
                arguments(rsaKeyFrom(RSA512Private), RS256),
                // 1024-bit RSA
                arguments(rsaKeyFrom(RSA1024Private), RS256),
                arguments(rsaKeyFrom(RSA1024Private), RS384),
                arguments(rsaKeyFrom(RSA1024Private), RS512),
                arguments(rsaKeyFrom(RSA1024Private), PS256),
                arguments(rsaKeyFrom(RSA1024Private), PS384),
                // 2048-bit RSA
                arguments(rsaKeyFrom(RSA2048Private), RS256),
                arguments(rsaKeyFrom(RSA2048Private), RS384),
                arguments(rsaKeyFrom(RSA2048Private), RS512),
                arguments(rsaKeyFrom(RSA2048Private), PS256),
                arguments(rsaKeyFrom(RSA2048Private), PS384),
                arguments(rsaKeyFrom(RSA2048Private), PS512),
                // 3072-bit RSA
                arguments(rsaKeyFrom(RSA3072Private), RS256),
                arguments(rsaKeyFrom(RSA3072Private), RS384),
                arguments(rsaKeyFrom(RSA3072Private), RS512),
                arguments(rsaKeyFrom(RSA3072Private), PS256),
                arguments(rsaKeyFrom(RSA3072Private), PS384),
                arguments(rsaKeyFrom(RSA3072Private), PS512),
                // 4096-bit RSA
                arguments(rsaKeyFrom(RSA4096Private), RS256),
                arguments(rsaKeyFrom(RSA4096Private), RS384),
                arguments(rsaKeyFrom(RSA4096Private), RS512),
                arguments(rsaKeyFrom(RSA4096Private), PS256),
                arguments(rsaKeyFrom(RSA4096Private), PS384),
                arguments(rsaKeyFrom(RSA4096Private), PS512),
                // EC
                arguments(ecKeyFrom(PRIME256v1PrivateSEC1), ES256),
                arguments(ecKeyFrom(PRIME256v1PrivatePKCS8), ES256),
                arguments(ecKeyFrom(SECP256K1PrivateSEC1), ES256K),
                arguments(ecKeyFrom(SECP256K1PrivatePKCS8), ES256K),
                arguments(ecKeyFrom(SECP384R1PrivateSEC1), ES384),
                arguments(ecKeyFrom(SECP384R1PrivatePKCS8), ES384),
                arguments(ecKeyFrom(SECP521R1PrivateSEC1), ES512),
                arguments(ecKeyFrom(SECP521R1PrivatePCKS8), ES512),
                // OKP
                arguments(okpPrivateKeyFrom(ED448Private), EdDSA),
                arguments(okpPrivateKeyFrom(ED25519Private), EdDSA),
                // Octet Sequences - should be able to sign and verify with any supported algorithm
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(128).generate()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(128).generate()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(128).generate()), HS512),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(192).generate()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(192).generate()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(192).generate()), HS512),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(256).generate()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(256).generate()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(256).generate()), HS512),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(384).generate()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(384).generate()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(384).generate()), HS512),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(512).generate()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(512).generate()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKeyGenerator(512).generate()), HS512),
                arguments(JWKKeyFactory.from(new OctetSequenceKey.Builder("secret123".getBytes()).build()), HS256),
                arguments(JWKKeyFactory.from(new OctetSequenceKey.Builder("secret123".getBytes()).build()), HS384),
                arguments(JWKKeyFactory.from(new OctetSequenceKey.Builder("secret123".getBytes()).build()), HS512)
        );
    }
}
