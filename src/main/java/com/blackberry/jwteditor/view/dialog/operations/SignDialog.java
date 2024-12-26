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

package com.blackberry.jwteditor.view.dialog.operations;

import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.jose.JWSFactory;
import com.blackberry.jwteditor.model.jose.JWSFactory.SigningUpdateMode;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.operations.Attacks;
import com.blackberry.jwteditor.utils.Utils;
import com.blackberry.jwteditor.view.utils.ErrorLoggingActionListenerFactory;
import com.nimbusds.jose.JWSAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

import static com.blackberry.jwteditor.model.jose.JWSFactory.SigningUpdateMode.*;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 * Sign and Attack > Embedded JWK dialog from the Editor tab
 */
public class SignDialog extends OperationDialog<JWS> {

    public enum Mode {
        NORMAL("sign_dialog_title"),
        EMBED_JWK("embed_jwk_attack_dialog_title");

        private final String titleResourceId;

        Mode(String titleResourceId) {
            this.titleResourceId = titleResourceId;
        }
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Key> comboBoxSigningKey;
    private JComboBox<JWSAlgorithm> comboBoxSigningAlgorithm;
    private JPanel panelOptions;
    private JRadioButton radioButtonUpdateGenerateAlg;
    private JRadioButton radioButtonUpdateGenerateJWT;
    private JRadioButton radioButtonUpdateGenerateNone;

    private final Mode mode;
    private JWS jws;

    public SignDialog(
            Window parent,
            ErrorLoggingActionListenerFactory actionListenerFactory,
            List<Key> signingKeys,
            JWS jws,
            Mode mode) {
        super(parent, mode.titleResourceId);
        this.jws = jws;
        this.mode = mode;

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(actionListenerFactory.from(e -> onOK()));
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        // Convert the signingKeys from a List to an Array
        Key[] signingKeysArray = new Key[signingKeys.size()];
        signingKeys.toArray(signingKeysArray);

        // Populate the signing keys dropdown
        comboBoxSigningKey.setModel(new DefaultComboBoxModel<>(signingKeysArray));

        // Set an event handler to update the signing algorithms dropdown when the selected key changes
        comboBoxSigningKey.addActionListener(e -> {
            Key selectedKey = (Key) comboBoxSigningKey.getSelectedItem();
            //noinspection ConstantConditions
            comboBoxSigningAlgorithm.setModel(new DefaultComboBoxModel<>(selectedKey.getSigningAlgorithms()));
            buttonOK.setEnabled(true);
        });

        // Set the signing key to the first entry, also triggering the event handler
        comboBoxSigningKey.setSelectedIndex(0);

        // If the dialog is being used for the embedded JWK attack, hide the Header Options
        if (mode != Mode.NORMAL) {
            panelOptions.setVisible(false);
        }
    }

    @Override
    public JWS getJWT() {
        return jws;
    }

    @SuppressWarnings("ConstantConditions")
    private void onOK() {
        // Get the selected signing key and algorithm
        JWKKey selectedKey = (JWKKey) comboBoxSigningKey.getSelectedItem();
        JWSAlgorithm selectedAlgorithm = (JWSAlgorithm) comboBoxSigningAlgorithm.getSelectedItem();

        // Get the header update mode based on the selected radio button, convert to the associated enum value
        SigningUpdateMode signingUpdateMode;

        if (radioButtonUpdateGenerateAlg.isSelected()) {
            signingUpdateMode = UPDATE_ALGORITHM_ONLY;
        } else if (radioButtonUpdateGenerateJWT.isSelected()) {
            signingUpdateMode = UPDATE_ALGORITHM_TYPE_AND_KID;
        } else {
            signingUpdateMode = DO_NOT_MODIFY_HEADER;
        }

        // Perform a signing operation or the embedded JWK attack based on the dialog mode
        try {
            if (mode == Mode.NORMAL) {
                jws = JWSFactory.sign(selectedKey, selectedAlgorithm, signingUpdateMode, jws);
            } else if (mode == Mode.EMBED_JWK) {
                jws = Attacks.embeddedJWK(jws, selectedKey, selectedAlgorithm);
            }
        } catch (SigningException | NoSuchFieldException | IllegalAccessException e) {
            jws = null;
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    Utils.getResourceString("error_title_unable_to_sign"),
                    WARNING_MESSAGE
            );
        } finally {
            dispose();
        }
    }
}
