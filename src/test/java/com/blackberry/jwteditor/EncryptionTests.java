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
import com.blackberry.jwteditor.model.jose.JWEFactory;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.exceptions.DecryptionException;
import com.blackberry.jwteditor.model.jose.exceptions.EncryptionException;
import com.blackberry.jwteditor.model.keys.PasswordKey;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Security;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class EncryptionTests {
    private static final String TEST_JWS = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJUZXN0In0.WVLalefVZ5Rj991Cjgh0qBjKSIQaqC_CgN3b-30GKpQ";

    @BeforeAll
    static void addBouncyCastle() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void passwordEncryption() throws ParseException, EncryptionException, DecryptionException {
        boolean atLeastOne = false;
        JWS jwsObject = JWS.parse(TEST_JWS);

        PasswordKey key = new PasswordKey("Test", "Test", 8, 1000);
        if (key.canEncrypt()) {
            for (JWEAlgorithm kek : key.getKeyEncryptionKeyAlgorithms()) {
                for (EncryptionMethod cek : key.getContentEncryptionKeyAlgorithms(kek)) {
                    JWE jwe = JWEFactory.encrypt(jwsObject, key, kek, cek);
                    jwe.decrypt(key);
                    atLeastOne = true;
                }
            }
        }
        assertThat(atLeastOne).isTrue();
    }
}
