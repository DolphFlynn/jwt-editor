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

package com.blackberry.jwteditor.view;

import burp.api.montoya.ui.UserInterface;
import burp.config.BurpConfig;
import com.blackberry.jwteditor.model.keys.KeysModel;
import com.blackberry.jwteditor.model.persistence.KeysModelPersistence;
import com.blackberry.jwteditor.model.tokens.TokensModel;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.config.ConfigView;
import com.blackberry.jwteditor.view.keys.KeysView;
import com.blackberry.jwteditor.view.rsta.RstaFactory;
import com.blackberry.jwteditor.view.tokens.TokensView;

import javax.swing.*;
import java.awt.*;

public class SuiteView {
    private final Window parent;
    private final KeysModelPersistence keysModelPersistence;
    private final KeysModel keysModel;
    private final TokensModel tokensModel;
    private final RstaFactory rstaFactory;
    private final BurpConfig burpConfig;
    private final UserInterface userInterface;
    private final boolean isProVersion;

    private JPanel panel;
    private KeysView keysView;
    private ConfigView configView;
    private TokensView tokensView;

    public SuiteView(
            Window parent,
            KeysModelPersistence keysModelPersistence,
            KeysModel keysModel,
            TokensModel tokensModel,
            RstaFactory rstaFactory,
            BurpConfig burpConfig,
            UserInterface userInterface,
            boolean isProVersion) {
        this.parent = parent;
        this.keysModelPersistence = keysModelPersistence;
        this.keysModel = keysModel;
        this.tokensModel = tokensModel;
        this.rstaFactory = rstaFactory;
        this.burpConfig = burpConfig;
        this.userInterface = userInterface;
        this.isProVersion = isProVersion;
    }

    /**
     * Get the name of the tab for display in BurpSuite
     * @return the tab name
     */
    public String getTabCaption() {
        return Utils.getResourceString("burp_tab");
    }

    /**
     * Get the view instance for display in BurpSuite
     * @return the view as a Component
     */
    public Component getUiComponent() {
        return panel;
    }

    private void createUIComponents() {
        keysView = new KeysView(
                parent,
                keysModelPersistence,
                keysModel,
                rstaFactory
        );
        configView = new ConfigView(burpConfig, userInterface, isProVersion, keysModel);
        tokensView = new TokensView(tokensModel, rstaFactory);
    }
}
