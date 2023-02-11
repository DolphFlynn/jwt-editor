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

package com.blackberry.jwteditor.model.keys;

import com.blackberry.jwteditor.model.jose.JWE;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.exceptions.DecryptionException;
import com.blackberry.jwteditor.model.jose.exceptions.VerificationException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class KeyRing {
    private final List<Key> keys;

    public KeyRing(List<Key> keys) {
        this.keys = keys;
    }

    public Optional<Key> findVerifyingKey(JWS jws) {
        for (Key key : keys) {
            for (JWSAlgorithm signingAlgorithm : key.getSigningAlgorithms()) {
                JWSHeader verificationInfo = new JWSHeader.Builder(signingAlgorithm).build();
                try {
                    if (jws.verify(key, verificationInfo)) {
                        return Optional.of(key);
                    }
                } catch (VerificationException e) {
                    // Verification failed for this key & algorithm pair
                }
            }
        }

        return Optional.empty();
    }

    public Optional<JWS> attemptDecryption(JWE jwe) throws ParseException {
        for (Key key : keys) {
            try {
                return Optional.of(jwe.decrypt(key));
            } catch (DecryptionException e) {
                //Decryption failed for this key
            }
        }

        return Optional.empty();
    }
}
