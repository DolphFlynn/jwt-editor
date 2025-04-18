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

package com.blackberry.jwteditor.model.jose;

import com.nimbusds.jose.util.Base64URL;

import java.util.List;

/**
 * Abstract class representing common elements of JWE/JWT
 */
public abstract class JOSEObject {
    final Header header;

    JOSEObject(Base64URL header) {
        this.header = new Header(header);
    }

    public Header header() {
        return header;
    }

    /**
     * Serialize the JWT/JWE to a string in compact serialization form
     * @return the compact serialized JWE/JWS
     */
    public abstract String serialize();

    public abstract List<Information> information();
}
