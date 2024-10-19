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
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.unmodifiableCollection;

/**
 * A container class for Key objects
 */
public class KeysModel implements KeysRepository {
    private final Map<String, Key> keys;
    private final Object lock;

    private final List<KeysModelListener> modelListeners;

    public KeysModel() {
        this.keys = new LinkedHashMap<>();
        this.modelListeners = new ArrayList<>();
        this.lock = new Object();
    }

    public Iterable<Key> keys() {
        synchronized (lock) {
            return unmodifiableCollection(keys.values());
        }
    }

    public void addKeyModelListener(KeysModelListener modelListener) {
        this.modelListeners.add(modelListener);
    }

    public static KeysModel parse(String json) throws ParseException {
        KeysModel keysModel = new KeysModel();

        try {
            JSONArray savedKeys = new JSONArray(json);

            for (Object savedKey : savedKeys) {
                Key key = Key.fromJSONObject((JSONObject) savedKey);
                keysModel.addKey(key);
            }

            return keysModel;
        } catch (UnsupportedKeyException | ParseException e) {
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

    @Override
    public List<Key> getSigningKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canSign).toList();
        }
    }

    @Override
    public List<Key> getVerificationKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canVerify).toList();
        }
    }

    @Override
    public List<Key> getEncryptionKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canEncrypt).toList();
        }
    }

    @Override
    public List<Key> getDecryptionKeys() {
        synchronized (lock) {
            return keys.values().stream().filter(Key::canDecrypt).toList();
        }
    }

    public void addKey(Key key) {
        Key oldKey;

        synchronized (lock) {
            oldKey = keys.put(key.getID(), key);
        }

        for (KeysModelListener modelListener : modelListeners) {
            if (oldKey != null) {
                modelListener.notifyKeyDeleted(oldKey);
            }

            modelListener.notifyKeyInserted(key);
        }
    }

    private int findIndexOfKeyWithId(String id) {
        int i = 0;

        for (Key key : keys.values()) {
            if (key.getID().equals(id)) {
                return i;
            }

            i++;
        }

        return -1;
    }

    public void deleteKey(String keyId) {
        int rowIndex;

        synchronized (lock) {
            rowIndex = findIndexOfKeyWithId(keyId);
            keys.remove(keyId);
        }

        if (rowIndex >= 0) {
            for (KeysModelListener modelListener : this.modelListeners) {
                modelListener.notifyKeyDeleted(rowIndex);
            }
        }
    }

    public void deleteKeys(int[] indices) {
        synchronized (lock) {
            List<String> idsToDelete = IntStream.of(indices).mapToObj(this::getKey).map(Key::getID).toList();

            for (String id : idsToDelete) {
                deleteKey(id);
            }
        }
    }

    public Key getKey(int index) {
        synchronized (lock) {
            String key = (String) keys.keySet().toArray()[index];
            return keys.get(key);
        }
    }

    @Override
    public Key getKey(String keyId) {
        synchronized (lock) {
            return keys.get(keyId);
        }
    }

    public boolean keyExists(String keyId) {
        synchronized (lock) {
            return keys.get(keyId) != null;
        }
    }
}
