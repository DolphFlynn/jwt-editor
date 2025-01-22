/*
Author : Dolph Flynn

Copyright 2025 Dolph Flynn

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

package com.blackberry.jwteditor.view.tokens;

import com.blackberry.jwteditor.view.rsta.RstaFactory;
import com.blackberry.jwteditor.view.utils.RunEDTActionOnFirstRenderHierarchyListener;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;

public class TokensView {

    private final RstaFactory rstaFactory;

    private JPanel panel;
    private JTable tokenTable;
    private RSyntaxTextArea textAreaPayload;
    private JSplitPane splitPane;

    public TokensView(RstaFactory rstaFactory) {
        this.rstaFactory = rstaFactory;

        panel.addHierarchyListener(new RunEDTActionOnFirstRenderHierarchyListener(
                panel,
                () -> splitPane.setDividerLocation(0.5)
        ));

        textAreaPayload.setEditable(false);
        textAreaPayload.setText("""
                {
                  "sub": "1234567890",
                  "name": "John Doe",
                  "iat": 1516239022
                }""");
    }

    private void createUIComponents() {
        textAreaPayload = rstaFactory.buildDefaultTextArea();
    }
}
