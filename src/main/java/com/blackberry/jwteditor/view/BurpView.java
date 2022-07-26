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

import burp.IBurpExtenderCallbacks;
import burp.ITab;
import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.config.ProxyConfig;
import com.blackberry.jwteditor.model.persistence.ProxyConfigPersistence;
import com.blackberry.jwteditor.presenter.BurpPresenter;
import com.blackberry.jwteditor.presenter.PresenterStore;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.utils.ThemeDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import static java.awt.Image.SCALE_SMOOTH;

/**
 * View class for the Burp extender ITab
 */
public class BurpView implements ITab {
    private final BurpPresenter presenter;
    private final Window parent;
    private final PresenterStore presenters;
    private final IBurpExtenderCallbacks callbacks;
    private final KeysModel keysModel;
    private final RstaFactory rstaFactory;

    private JPanel panel;
    private JLabel configurationButton;
    private JPanel headerPanel;
    private KeysView keysView;

    public BurpView(
            Window parent,
            PresenterStore presenters,
            IBurpExtenderCallbacks callbacks,
            KeysModel keysModel,
            RstaFactory rstaFactory,
            ProxyConfigPersistence proxyConfigPersistence,
            ProxyConfig proxyConfig) {
        this.parent = parent;
        this.presenters = presenters;
        this.callbacks = callbacks;
        this.keysModel = keysModel;
        this.rstaFactory = rstaFactory;

        // Initialise the presenter
        presenter = new BurpPresenter(
                this,
                presenters,
                proxyConfigPersistence,
                proxyConfig
        );

        configurationButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                presenter.onButtonConfigurationClick();
            }
        });
    }

    /**
     * Get the name of the tab for display in BurpSuite
     * @return the tab name
     */
    @Override
    public String getTabCaption() {
        return Utils.getResourceString("burp_keys_tab");
    }

    /**
     * Get the view instance for display in BurpSuite
     * @return the view as a Component
     */
    @Override
    public Component getUiComponent() {
        return panel;
    }

    /**
     * Custom form initialisation
     */
    private void createUIComponents() {
        keysView = new KeysView(
                parent,
                presenters,
                callbacks,
                keysModel,
                rstaFactory
        );

        configurationButton = new ConfigurationButton();
    }

    private static class ConfigurationButton extends JLabel {
        private static final String LIGHT_ICON_PATH = "/resources/Media/configuration.png"; //NON-NLS
        private static final String DARK_ICON_PATH = "/resources/Media/dark/configuration.png"; //NON-NLS
        private static final URL LIGHT_CONFIGURATION_ICON_URL = KeysView.class.getResource(LIGHT_ICON_PATH);
        private static final URL DARK_CONFIGURATION_ICON_URL = KeysView.class.getResource(DARK_ICON_PATH);

        private final Image lightImage;
        private final Image darkImage;
        private final Boolean initialized;

        ConfigurationButton() {
            this.lightImage = LIGHT_CONFIGURATION_ICON_URL == null ? null : new ImageIcon(LIGHT_CONFIGURATION_ICON_URL).getImage();
            this.darkImage = DARK_CONFIGURATION_ICON_URL == null ? null : new ImageIcon(DARK_CONFIGURATION_ICON_URL).getImage();
            this.initialized = true;

            HierarchyListener hierarchyListener = new HierarchyListener() {
                @Override
                public void hierarchyChanged(HierarchyEvent e) {
                    if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED && e.getComponent().isShowing()) {
                        scaleAndUpdateIcon();
                        ConfigurationButton.this.removeHierarchyListener(this);
                    }
                }
            };

            addHierarchyListener(hierarchyListener);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            scaleAndUpdateIcon();
        }

        private void scaleAndUpdateIcon() {
            if (initialized == null) {
                return;
            }

            // Fallback in case icon location or name change
            if (lightImage == null || darkImage == null) {
                setText("Config"); //NON-NLS
                return;
            }

            int size = this.getHeight();

            if (size > 0) {
                Image image = ThemeDetector.isLightTheme() ? lightImage : darkImage;
                Image scaledIcon = image.getScaledInstance(size, size, SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledIcon));
            }
        }
    }
}
