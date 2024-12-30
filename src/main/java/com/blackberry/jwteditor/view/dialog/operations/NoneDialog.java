/*

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

package com.blackberry.jwteditor.view.dialog.operations;

import burp.api.montoya.logging.Logging;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;

import javax.swing.*;
import java.awt.*;

public class NoneDialog extends OperationDialog<JWS> {
    private static final String[] NONE_ALGORITHM_VALUES = {"none", "None", "NONE", "nOnE"};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboBoxAlgorithm;

    public NoneDialog(Window parent, Logging logging, JWS jws) {
        super(parent, logging, "none_attack_dialog_title", jws);

        configureUI(contentPane, buttonOK, buttonCancel);

        comboBoxAlgorithm.setModel(new DefaultComboBoxModel<>(NONE_ALGORITHM_VALUES));
        comboBoxAlgorithm.setSelectedIndex(0);
    }

    @Override
    JWS performOperation() {
        String selectedAlgorithm = (String) comboBoxAlgorithm.getSelectedItem();
        return Attacks.noneSigning(jwt, selectedAlgorithm);
    }
}
