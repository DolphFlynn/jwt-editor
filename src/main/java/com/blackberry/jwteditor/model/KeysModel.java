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

package com.blackberry.jwteditor.model;

import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.presenter.KeysPresenter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableCollection;

/**
 * A container class for Key objects
 */
public class KeysModel {
    private final Map<String, Key> keys = new LinkedHashMap<>();
    private final Object lock = new Object();

    private KeysPresenter presenter;

    public Iterable<Key> keys() {
        synchronized (lock) {
            return unmodifiableCollection(keys.values());
        }
    }

    /**
     * Parse a JSON string to a KeysModel
     *
     * @param json JSON string containing encoded keys
     * @return the KeysModel parsed from the JSON
     * @throws ParseException if parsing fails
     */
    public static KeysModel parse(String json) throws ParseException {
        KeysModel keysModel = new KeysModel();

        try {
            JSONArray savedKeys = new JSONArray(json);

            for (Object savedKey : savedKeys) {
                Key key = Key.fromJSONObject((JSONObject) savedKey);
                keysModel.addKey(key);
            }

            return keysModel;
        } catch (Key.UnsupportedKeyException | ParseException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    /**
     * Convert the KeysModel to a JSON string
     *
     * @return JSON string representation of the KeysModel
     */
    public String serialize() {
        JSONArray jsonArray = new JSONArray();

        synchronized (lock) {
            keys.values().stream().map(Key::toJSONObject).forEach(jsonArray::put);
        }

        return jsonArray.toString();
    }

    /**
     * Associate a UI presenter with this model that will be notified when the model changes
     *
     * @param presenter presenter to associate
     */
    public void setPresenter(KeysPresenter presenter) {
        this.presenter = presenter;
    }

    public List<Key> getSigningKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canSign).toList();
        }
    }

    public List<Key> getVerificationKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canVerify).toList();
        }
    }

    public List<Key> getEncryptionKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canEncrypt).toList();
        }
    }

    public List<Key> getDecryptionKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canDecrypt).toList();
        }
    }

    /**
     * Add a key to the model
     *
     * @param key key to add
     */
    public void addKey(Key key) {
        synchronized (lock) {
            keys.put(key.getID(), key);
        }

        if (presenter != null) {
            presenter.onModelUpdated();
        }
    }

    /**
     * Remove a key from the model by id
     *
     * @param keyId key id to remove
     */
    public void deleteKey(String keyId) {
        synchronized (lock) {
            keys.remove(keyId);
        }

        if (presenter != null) {
            presenter.onModelUpdated();
        }
    }

    /**
     * Remove a set of keys from the model by id
     *
     * @param indicies indicies of keys to remove
     */
    public void deleteKeys(int[] indicies) {
        synchronized (lock) {
            for (int index : indicies) {
                deleteKey(getKey(index).getID());
            }
        }
    }

    /**
     * Get a key from the model by index
     *
     * @param index index of key to retrieve
     * @return retrieved key
     */
    public Key getKey(int index) {
        synchronized (lock) {
            String key = (String) keys.keySet().toArray()[index];
            return keys.get(key);
        }
    }

    /**
     * Get a key from the model by index
     *
     * @param keyId ID of key to retrieve
     * @return retrieved key
     */
    public Key getKey(String keyId) {
        synchronized (lock) {
            return keys.get(keyId);
        }
    }
}
