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

/**
 * High-level operations on JWE/JWS
 */
public class Operations {

    public enum SigningUpdateMode {
        NONE,
        ALG,
        JWT
    }

    /**
     * Sign a JWS with a JWK
     *
     * @param jws the JWS to sign
     * @param key the JWK to sign the JWS with
     * @param algorithm the algorithm to sign with
     * @param signingUpdateMode the header update mode
     * @return the signed JWS
     * @throws SigningException if signing fails
     */
    public static JWS sign(JWS jws, JWKKey key, JWSAlgorithm algorithm, SigningUpdateMode signingUpdateMode) throws SigningException {

        // Build a new JWS header with the algorithm to use for signing
        JWSHeader signingInfo = new JWSHeader.Builder(algorithm).build();

        Base64URL encodedHeader;
        JSONObject jsonHeader;
        switch(signingUpdateMode){
            // Don't update the header
            case NONE:
                encodedHeader = jws.getEncodedHeader();
                break;
            // Update or insert the 'alg' field
            case ALG:
                try {
                    jsonHeader = new JSONObject(jws.getHeader());
                }
                catch (JSONException e) {
                    jsonHeader = new JSONObject();
                }
                jsonHeader.put("alg", algorithm.getName()); //NON-NLS
                encodedHeader = Base64URL.encode(jsonHeader.toString());
                break;
            // Update or insert 'alg', 'typ' and 'kid'
            case JWT:
                try {
                    jsonHeader = new JSONObject(jws.getHeader());
                }
                catch (JSONException e) {
                    jsonHeader = new JSONObject();
                }
                jsonHeader.put("alg", algorithm.getName()); //NON-NLS
                jsonHeader.put("typ", "JWT"); //NON-NLS
                jsonHeader.put("kid", key.getID()); //NON-NLS
                encodedHeader = Base64URL.encode(jsonHeader.toString());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + signingUpdateMode);
        }
        // Do the signing operation
        Base64URL payload = jws.getEncodedPayload();

        return JWSFactory.sign(key, encodedHeader, payload, signingInfo);
    }
}
