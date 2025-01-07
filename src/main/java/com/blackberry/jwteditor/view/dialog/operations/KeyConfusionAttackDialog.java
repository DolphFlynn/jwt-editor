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

import burp.api.montoya.logging.Logging;
import com.blackberry.jwteditor.exceptions.PemException;
import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.model.keys.JWKKey;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.operations.Attacks;
import com.nimbusds.jose.JWSAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.blackberry.jwteditor.view.dialog.operations.LastSigningKeys.Signer.KEY_CONFUSION;

/**
 * Attack > HMAC Key Confusion dialog from the Editor tab
 */
public class KeyConfusionAttackDialog extends OperationDialog<JWS> {
    private static final JWSAlgorithm[] ALGORITHMS = {JWSAlgorithm.HS256, JWSAlgorithm.HS384, JWSAlgorithm.HS512};

    private final LastSigningKeys lastSigningKeys;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Key> comboBoxSigningKey;
    private JComboBox<JWSAlgorithm> comboBoxSigningAlgorithm;
    private JCheckBox checkBoxTrailingNewline;

    public KeyConfusionAttackDialog(
            Window parent,
            Logging logging,
            List<Key> signingKeys,
            LastSigningKeys lastSigningKeys,
            JWS jws) {
        super(parent, logging, "key_confusion_attack_dialog_title", jws, "error_title_unable_to_sign");

        this.lastSigningKeys = lastSigningKeys;

        configureUI(contentPane, buttonOK, buttonCancel);

        int lastUsedKeyIndex = lastSigningKeys.lastKeyFor(KEY_CONFUSION).map(signingKeys::indexOf).orElse(-1);
        lastUsedKeyIndex = lastUsedKeyIndex == -1 ? 0 : lastUsedKeyIndex;

        comboBoxSigningKey.setModel(new DefaultComboBoxModel<>(signingKeys.toArray(Key[]::new)));
        comboBoxSigningKey.setSelectedIndex(lastUsedKeyIndex);

        comboBoxSigningAlgorithm.setModel(new DefaultComboBoxModel<>(ALGORITHMS));
    }

    @Override
    JWS performOperation() throws SigningException, PemException, UnsupportedKeyException {
        JWKKey selectedKey = (JWKKey) comboBoxSigningKey.getSelectedItem();
        JWSAlgorithm selectedAlgorithm = (JWSAlgorithm) comboBoxSigningAlgorithm.getSelectedItem();

        lastSigningKeys.recordKeyUse(KEY_CONFUSION, selectedKey);

        return Attacks.hmacKeyConfusion(jwt, selectedKey, selectedAlgorithm, checkBoxTrailingNewline.isSelected());
    }
}
