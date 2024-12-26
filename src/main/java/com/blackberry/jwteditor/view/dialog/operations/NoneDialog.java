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

import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class NoneDialog extends OperationDialog<JWS> {
    private static final String[] NONE_ALGORITHM_VALUES = {"none", "None", "NONE", "nOnE"};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboBoxAlgorithm;
    private JWS jws;

    /**
     * Show the none attack dialog
     * @param parent the parent for the dialog
     * @param jws the content to remove signature from
     */
    public NoneDialog(Window parent, JWS jws) {
        super(parent, "none_attack_dialog_title");
        this.jws = jws;

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        comboBoxAlgorithm.setModel(new DefaultComboBoxModel<>(NONE_ALGORITHM_VALUES));
        comboBoxAlgorithm.setSelectedIndex(0);
    }

    @Override
    public JWS getJWT(){
        return jws;
    }

    /**
     * Handler for OK button pressed. Remove signature and update 'alg' to selected parameter
     */
    private void onOK() {
        String selectedAlgorithm = (String) comboBoxAlgorithm.getSelectedItem();

        jws = Attacks.noneSigning(jws, selectedAlgorithm);

        dispose();
    }
}
