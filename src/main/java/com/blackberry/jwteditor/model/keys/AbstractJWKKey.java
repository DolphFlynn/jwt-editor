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

import com.nimbusds.jose.jwk.JWK;
import org.json.JSONObject;

import java.util.Map;

abstract class AbstractJWKKey implements JWKKey {
    private final JWK jwk;
    private final KeyType keyType;

    AbstractJWKKey(JWK jwk, KeyType keyType) {
        this.jwk = jwk;
        this.keyType = keyType;
    }

    @Override
    public JWK getJWK() {
        return jwk;
    }

    @Override
    public KeyType getKeyType() {
        return keyType;
    }

    @Override
    public String getID() {
        return jwk.getKeyID();
    }

    @Override
    public boolean isPublic() {
        return keyType != KeyType.OCT;
    }

    @Override
    public boolean isPrivate() {
        return jwk.isPrivate();
    }

    @Override
    public boolean canDecrypt() {
        return canEncrypt() && isPrivate();
    }

    @Override
    public boolean hasJWK() {
        return true;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> jwkMap = jwk.toJSONObject();

        for(String k : jwkMap.keySet()){
            jsonObject.put(k, jwkMap.get(k));
        }

        return jsonObject;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getID(), getDescription());
    }
}
