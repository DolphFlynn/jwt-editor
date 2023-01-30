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

package com.blackberry.jwteditor.presenter;

import com.blackberry.jwteditor.model.config.BurpConfig;
import com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence;
import com.blackberry.jwteditor.view.BurpView;
import com.blackberry.jwteditor.view.dialog.config.BurpConfigDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Presenter for the Burp view
 */
public class BurpPresenter extends Presenter {
    private final Window window;
    private final ProxyConfigPersistence proxyConfigPersistence;
    private final BurpConfig burpConfig;

    /**
     * Create a new BurpPresenter
     *
     * @param burpView               the BurpView to associate with the presenter
     * @param presenters             the shared store of all presenters
     * @param burpConfig            Configuration for proxy listener
     * @param proxyConfigPersistence Used to persist proxy configuration
     */
    public BurpPresenter(BurpView burpView,
                         PresenterStore presenters,
                         ProxyConfigPersistence proxyConfigPersistence,
                         BurpConfig burpConfig) {
        this.window = SwingUtilities.getWindowAncestor(burpView.getUiComponent());
        this.proxyConfigPersistence = proxyConfigPersistence;
        this.burpConfig = burpConfig;

        presenters.register(this);
    }

    /**
     * Handler for button clicks for configuration
     */
    public void onButtonConfigurationClick() {
        BurpConfigDialog dialog = new BurpConfigDialog(window, burpConfig);

        // Display the dialog
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
        // Block here until the dialog returns

        proxyConfigPersistence.save(burpConfig);
    }
}
