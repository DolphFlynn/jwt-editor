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
import com.blackberry.jwteditor.model.config.BurpConfig;
import com.blackberry.jwteditor.model.config.HighlightColor;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Interface for loading and saving ProxyConfig
 */
public class ProxyConfigPersistence {
    public static final String PROXY_LISTENER_SETTINGS_NAME = "com.blackberry.jwteditor.proxy"; //NON-NLS

    private static final String PROXY_LISTENER_ENABLED_KEY = "proxy_listener_enabled";  //NON-NLS
    private static final String PROXY_HISTORY_HIGHLIGHT_COLOR_KEY = "proxy_history_highlight_color";  //NON-NLS

    private final Preferences preferences;

    public ProxyConfigPersistence(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Loads and returns existing proxy config or creates a new instance if no configuration exists
     *
     * @return instance of proxy config
     */
    public BurpConfig loadOrCreateNew() {
        String json = preferences.getString(PROXY_LISTENER_SETTINGS_NAME);

        // If parse fails, create a new proxy config
        if (json != null) {
            try {
                JSONObject parsedObject = new JSONObject(json);

                if (parsedObject.has(PROXY_LISTENER_ENABLED_KEY) && parsedObject.has(PROXY_HISTORY_HIGHLIGHT_COLOR_KEY)) {
                    boolean highlightJWT = (Boolean) parsedObject.get(PROXY_LISTENER_ENABLED_KEY);

                    String highlightColorName = (String) parsedObject.get(PROXY_HISTORY_HIGHLIGHT_COLOR_KEY);
                    HighlightColor highlightColor = HighlightColor.from(highlightColorName);

                    return new BurpConfig(highlightJWT, highlightColor);
                }
            } catch (ClassCastException | JSONException ignored) {
            }
        }

        return new BurpConfig();
    }

    /**
     * Saves proxy configuration
     *
     * @param model proxy config to be saved
     */
    public void save(BurpConfig model) {
        // Serialise the proxy config and save
        JSONObject proxyConfigJson = new JSONObject();
        proxyConfigJson.put(PROXY_LISTENER_ENABLED_KEY, model.proxyConfig().highlightJWT());
        proxyConfigJson.put(PROXY_HISTORY_HIGHLIGHT_COLOR_KEY, model.proxyConfig().highlightColor().burpColor);

        preferences.setString(PROXY_LISTENER_SETTINGS_NAME, proxyConfigJson.toString());
    }
}
