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

import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeysModel;

import java.util.concurrent.atomic.AtomicInteger;

import static com.blackberry.jwteditor.KeyLoader.*;

class KeysModelBuilder {
    private final AtomicInteger keyId = new AtomicInteger();
    private final KeysModel model = new KeysModel();

    KeysModelBuilder withECKey(String pem) {
        model.addKey(loadECKey(pem, nextKeyId()));
        return this;
    }

    KeysModelBuilder withRSAKey(String pem) {
        model.addKey(loadRSAKey(pem, nextKeyId()));
        return this;
    }

    KeysModelBuilder withOKPKey(String pem) {
        model.addKey(loadOKPKey(pem, nextKeyId()));
        return this;
    }

    KeysModelBuilder withKey(Key key) {
        model.addKey(key);
        return this;
    }

    KeysModel build() {
        return model;
    }

    private String nextKeyId() {
        return Integer.toString(keyId.incrementAndGet());
    }

    static KeysModelBuilder keysModel() {
        return new KeysModelBuilder();
    }
}
