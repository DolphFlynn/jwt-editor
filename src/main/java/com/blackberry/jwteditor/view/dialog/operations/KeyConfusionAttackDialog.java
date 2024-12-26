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

import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.jose.JWS;
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

/**
 * Attack > HMAC Key Confusion dialog from the Editor tab
 */
public class KeyConfusionAttackDialog extends OperationDialog<JWS> {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Key> comboBoxSigningKey;
    private JComboBox<JWSAlgorithm> comboBoxSigningAlgorithm;
    private JCheckBox checkBoxTrailingNewline;

    private JWS jws;

    public KeyConfusionAttackDialog(
            Window parent,
            ErrorLoggingActionListenerFactory actionListenerFactory,
            List<Key> signingKeys,
            JWS jws) {
        super(parent, "key_confusion_attack_dialog_title");
        this.jws = jws;

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

        // Convert appropriate signingKeys List to an Array
        Key[] signingKeysArray = signingKeys
                .stream()
                .filter(Key::canConvertToPem)
                .toArray(Key[]::new);

        // Populate the dropdown with the signing keys
        comboBoxSigningKey.setModel(new DefaultComboBoxModel<>(signingKeysArray));

        // Populate the Signing Algorithm dropdown
        comboBoxSigningAlgorithm.setModel(new DefaultComboBoxModel<>(new JWSAlgorithm[] {JWSAlgorithm.HS256, JWSAlgorithm.HS384, JWSAlgorithm.HS512}));

        // Select the first signing key
        comboBoxSigningKey.setSelectedIndex(0);
    }

    @SuppressWarnings("ConstantConditions")
    private void onOK() {
        // Get the selected key and algorithm
        JWKKey selectedKey = (JWKKey) comboBoxSigningKey.getSelectedItem();
        JWSAlgorithm selectedAlgorithm = (JWSAlgorithm) comboBoxSigningAlgorithm.getSelectedItem();

        // Try to perform the attack, show dialog if this fails
        try{
            jws = Attacks.hmacKeyConfusion(jws, selectedKey, selectedAlgorithm, checkBoxTrailingNewline.isSelected());
        } catch (SigningException | PemException | UnsupportedKeyException e) {
            jws = null;
            JOptionPane.showMessageDialog(this, e.getMessage(), Utils.getResourceString("error_title_unable_to_sign"), JOptionPane.WARNING_MESSAGE);
        } finally {
            dispose();
        }
    }

    @Override
    public JWS getJWT(){
        return jws;
    }
}
