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

package com.blackberry.jwteditor.model.keys;

import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.nimbusds.jose.jwk.*;

public class JWKKeyFactory {

    /**
     * Construct a JWKKey from a nimbus-jose JWK object
     *
     * @param jwk JWK
     * @return JWKKey
     * @throws UnsupportedKeyException if the key type is not supported
     */
    public static JWKKey from(JWK jwk) throws UnsupportedKeyException {
        if (jwk instanceof RSAKey rsaKey) {
            return new RSAJWKKey(rsaKey);
        }

        if (jwk instanceof ECKey ecKey) {
            return new ECJWKKey(ecKey);
        }

        if (jwk instanceof OctetKeyPair octetKeyPair) {
            return new OctetKeyPairJWKKey(octetKeyPair);
        }

        if (jwk instanceof OctetSequenceKey octetSequenceKey) {
            return new OctetSequenceKeyJWKKey(octetSequenceKey);
        }

        throw new UnsupportedKeyException();
    }
}