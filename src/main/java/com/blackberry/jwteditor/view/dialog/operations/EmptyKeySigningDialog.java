/*

Copyright 2023 Dolph Flynn

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

import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;
import com.blackberry.jwteditor.utils.Utils;
import com.nimbusds.jose.JWSAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

public class EmptyKeySigningDialog extends JDialog {
    private static final JWSAlgorithm[] ALGORITHMS = {JWSAlgorithm.HS256, JWSAlgorithm.HS384, JWSAlgorithm.HS512};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<JWSAlgorithm> comboBoxAlgorithm;
    private JWS jws;

    public EmptyKeySigningDialog(Window parent, JWS jws) {
        super(parent);
        this.jws = jws;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setTitle(Utils.getResourceString("empty_key_signing_dialog_title"));

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        comboBoxAlgorithm.setModel(new DefaultComboBoxModel<>(ALGORITHMS));
        comboBoxAlgorithm.setSelectedIndex(0);
    }

    public JWS getJWS() {
        return jws;
    }

    private void onOK() {
        JWSAlgorithm selectedAlgorithm = (JWSAlgorithm) comboBoxAlgorithm.getSelectedItem();

        try {
            jws = Attacks.signWithEmptyKey(jws, selectedAlgorithm);
        } catch (SigningException | ParseException | UnsupportedKeyException e) {
            jws = null;
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    Utils.getResourceString("error_title_unable_to_sign"),
                    JOptionPane.WARNING_MESSAGE
            );
        }

        dispose();
    }

    /**
     * Called when the Cancel or X button is pressed. Destroy the window
     */
    private void onCancel() {
        dispose();
    }
}
