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

package com.blackberry.jwteditor;

import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.utils.PEMUtils;
import com.nimbusds.jose.jwk.JWK;

import java.util.concurrent.atomic.AtomicInteger;

class KeysModelBuilder {
    private final AtomicInteger keyId = new AtomicInteger();
    private final KeysModel model = new KeysModel();

    KeysModelBuilder withECKey(String pem) {
        try {
            JWK jwk = PEMUtils.pemToECKey(pem, Integer.toString(keyId.incrementAndGet()));
            model.addKey(new JWKKey(jwk));
        } catch (PemException | UnsupportedKeyException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    KeysModelBuilder withRSAKey(String pem) {
        try {
            JWK jwk = PEMUtils.pemToRSAKey(pem, Integer.toString(keyId.incrementAndGet()));
            model.addKey(new JWKKey(jwk));
        } catch (PemException | UnsupportedKeyException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    KeysModelBuilder withOKPKey(String pem) {
        try {
            JWK jwk = PEMUtils.pemToOctetKeyPair(pem, Integer.toString(keyId.incrementAndGet()));
            model.addKey(new JWKKey(jwk));
        } catch (PemException | UnsupportedKeyException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    KeysModelBuilder withKey(Key key) {
        model.addKey(key);
        return this;
    }

    KeysModel build() {
        return model;
    }

    static KeysModelBuilder keysModel() {
        return new KeysModelBuilder();
    }
}
