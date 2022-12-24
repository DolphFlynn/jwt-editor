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

package com.blackberry.jwteditor.view;

import com.blackberry.jwteditor.model.KeysModel;
import com.blackberry.jwteditor.model.persistence.KeysModelPersistence;
import com.blackberry.jwteditor.model.persistence.StandaloneKeysModelPersistence;
import com.blackberry.jwteditor.presenter.PresenterStore;
import com.blackberry.jwteditor.presenter.StandalonePresenter;
import com.blackberry.jwteditor.view.RstaFactory.DefaultRstaFactory;

import javax.swing.*;

/**
 * View class for standalone mode (i.e outside of BurpSuite)
 */
public class StandaloneView {

    public static final int TAB_ENTRY = 0;
    public static final int TAB_EDITOR = 1;

    private final PresenterStore presenters;

    private final StandalonePresenter presenter;

    private final JFrame parent;
    private JPanel panel;
    private EntryView entryView;
    private EditorView editorView;
    private KeysView keysView;
    private JTabbedPane tabbedPane;

    public StandaloneView(JFrame parent, PresenterStore presenters){
        this.parent = parent;
        this.presenters = presenters;

        // Initialise the presenter
        presenter = new StandalonePresenter(this, presenters);

        // Handle the selected tab changing
        tabbedPane.addChangeListener(e -> presenter.onTabChanged());

        // Set a JWS as the default editor content
        entryView.setText("eyJraWQiOiI5YjQxZWE3Zi0zYzZhLTRhMzctODRkOS0zZjA5MmRiZTIyMzkiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0IEpXVCJ9.IVKZwHqdVbf4GD-a754H_SPv4AiGz37lpXYwCONEA-g");//NON-NLS
    }

    /**
     * Get the currently selected tab
     * @return index of the currently selected tab
     */
    public int getActiveTab(){
        return tabbedPane.getSelectedIndex();
    }

    /**
     * Enable or disable the editor tab
     * @param enabled whether the editor tab should be enabled
     */
    public void setEditorTabEnabled(boolean enabled){
        tabbedPane.setEnabledAt(TAB_EDITOR, enabled);
    }

    /**
     * Custom form initialisation
     */
    private void createUIComponents() {
        RstaFactory rstaFactory = new DefaultRstaFactory();
        entryView = new EntryView(parent, presenters, rstaFactory);
        editorView = new EditorView(parent, presenters, rstaFactory);

        // Get the storage directory for the keystore (~/.jwt-editor) and create it if it doesn't exist
        KeysModelPersistence keysModelPersistence = new StandaloneKeysModelPersistence();
        KeysModel keysModel = keysModelPersistence.loadOrCreateNew();

        keysView = new KeysView(
                parent,
                presenters,
                keysModelPersistence,
                keysModel,
                rstaFactory
        );
    }

    /**
     * Get the view
     * @return the standalone view
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Get the view's parent JFrame
     * @return parent JFrame
     */
    @SuppressWarnings("unused")
    public JFrame getParent() {
        return parent;
    }
}
