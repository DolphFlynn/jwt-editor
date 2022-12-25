/*
Author : Dolph Flynn

Copyright 2022 Dolph Flynn

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

package com.blackberry.jwteditor.model.persistence;

import burp.api.montoya.persistence.Preferences;
import com.blackberry.jwteditor.model.KeysModel;

import java.text.ParseException;

public class BurpKeysModelPersistence implements KeysModelPersistence {
    private static final String KEYSTORE_SETTINGS_NAME = "com.blackberry.jwteditor.keystore";

    private final Preferences preferences;

    public BurpKeysModelPersistence(Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public KeysModel loadOrCreateNew() {
        String json = preferences.getString(KEYSTORE_SETTINGS_NAME);

        // If this fails (empty), create a new keystore
        if (json != null) {
            try {
                return KeysModel.parse(json);
            } catch (ParseException ignored) {
            }
        }

        return new KeysModel();
    }

    @Override
    public void save(KeysModel model) {
        // Serialise the keystore and save inside the active Burp session
        preferences.setString(KEYSTORE_SETTINGS_NAME, model.serialize());
    }
}
