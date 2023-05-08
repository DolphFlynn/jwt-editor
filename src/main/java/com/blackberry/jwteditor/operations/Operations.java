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

package com.blackberry.jwteditor.operations;

import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import org.json.JSONException;
import org.json.JSONObject;

import static com.nimbusds.jose.HeaderParameterNames.*;

/**
 * High-level operations on JWE/JWS
 */
public class Operations {

    public enum SigningUpdateMode {
        DO_NOT_MODIFY_HEADER,
        UPDATE_ALGORITHM_ONLY,
        UPDATE_ALGORITHM_TYPE_AND_KID;

        Base64URL buildEncodedHeader(JWS jws, JWKKey key, JWSAlgorithm algorithm) {
            return switch (this) {
                case DO_NOT_MODIFY_HEADER -> jws.getEncodedHeader();

                case UPDATE_ALGORITHM_ONLY -> {
                    JSONObject jsonHeader = parseHeader(jws);
                    jsonHeader.put(ALGORITHM, algorithm.getName());

                    yield Base64URL.encode(jsonHeader.toString());
                }

                case UPDATE_ALGORITHM_TYPE_AND_KID -> {
                    JSONObject jsonHeader = parseHeader(jws);
                    jsonHeader.put(ALGORITHM, algorithm.getName());
                    jsonHeader.put(TYPE, "JWT");
                    jsonHeader.put(KEY_ID, key.getID());

                    yield Base64URL.encode(jsonHeader.toString());
                }
            };
        }

        private static JSONObject parseHeader(JWS jws) {
            try {
                return new JSONObject(jws.getHeader());
            } catch (JSONException e) {
                return new JSONObject();
            }
        }
    }

    /**
     * Sign a JWS with a JWK
     *
     * @param jws               the JWS to sign
     * @param key               the JWK to sign the JWS with
     * @param algorithm         the algorithm to sign with
     * @param signingUpdateMode the header update mode
     * @return the signed JWS
     * @throws SigningException if signing fails
     */
    public static JWS sign(JWS jws, JWKKey key, JWSAlgorithm algorithm, SigningUpdateMode signingUpdateMode) throws SigningException {
        // Build a new JWS header with the algorithm to use for signing
        return JWSFactory.sign(
                key,
                signingUpdateMode.buildEncodedHeader(jws, key, algorithm),
                jws.getEncodedPayload(),
                new JWSHeader.Builder(algorithm).build()
        );
    }
}
