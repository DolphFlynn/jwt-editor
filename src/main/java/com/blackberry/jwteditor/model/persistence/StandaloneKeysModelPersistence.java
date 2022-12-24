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

import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class StandaloneKeysModelPersistence implements KeysModelPersistence {
    // On-disk storage constants
    private static final String KEYS_DIR = ".jwt-editor"; //NON-NLS
    private static final String KEYS_FILE = "keys.json"; //NON-NLS

    @Override
    public KeysModel loadOrCreateNew() {
        // Load keys from the key store file, or create an empty keystore if it doesn't exist
        try {
            Path keysFile = getKeysFile();
            String json = new String(Files.readAllBytes(keysFile), UTF_8);
            return KeysModel.parse(json);
        } catch (ParseException | IOException ignored) {
        }

        return new KeysModel();
    }

    @Override
    public void save(KeysModel model) {
        // Serialise the keystore and save to disk
        try {
            String json = Utils.prettyPrintJSON(model.serialize());
            Files.write(getKeysFile(), json.getBytes(UTF_8));
        } catch (IOException e) {
            System.out.println(Utils.getResourceString("error_save")); //NON-NLS
        }
    }

    /**
     * Get the filesystem folder for the key store
     *
     * @return Path to key store folder
     */
    private static Path getKeysDir(){
        return Paths.get(System.getProperty("user.home"), KEYS_DIR);
    }

    /**
     * Get the file on the filesystem for the key store
     *
     * @return Path to the key store
     */
    private static Path getKeysFile(){
        return Paths.get(getKeysDir().toString(), KEYS_FILE);
    }
}
