/*
Author : Dolph Flynn

Copyright 2024 Dolph Flynn

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

package com.blackberry.jwteditor.model.jose;

import com.nimbusds.jose.util.Base64URL;

public class JWSClaims {
    private final Base64URL encodedClaims;
    private final ClaimsType type;

    public JWSClaims(Base64URL encodedClaims, ClaimsType type) {
        this.encodedClaims = encodedClaims;
        this.type = type;
    }

    public Base64URL encoded() {
        return encodedClaims;
    }

    public String decoded() {
        return encodedClaims.decodeToString();
    }

    public ClaimsType type() {
        return type;
    }

    @Override
    public String toString() {
        return encodedClaims.toString();
    }
}
