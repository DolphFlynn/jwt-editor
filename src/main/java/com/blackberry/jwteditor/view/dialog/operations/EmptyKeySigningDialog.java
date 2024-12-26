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

import burp.api.montoya.logging.Logging;
import com.blackberry.jwteditor.exceptions.SigningException;
import com.blackberry.jwteditor.exceptions.UnsupportedKeyException;
import com.blackberry.jwteditor.model.jose.JWS;
import com.blackberry.jwteditor.operations.Attacks;
import com.nimbusds.jose.JWSAlgorithm;

import javax.swing.*;
import java.awt.*;

import static com.nimbusds.jose.JWSAlgorithm.*;

public class EmptyKeySigningDialog extends OperationDialog<JWS> {
    private static final JWSAlgorithm[] ALGORITHMS = {HS256, HS384, HS512};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<JWSAlgorithm> comboBoxAlgorithm;

    public EmptyKeySigningDialog(Window parent, Logging logging, JWS jws) {
        super(parent, logging, "empty_key_signing_dialog_title", jws, "error_title_unable_to_sign");

        configureUI(contentPane, buttonOK, buttonCancel);

        comboBoxAlgorithm.setModel(new DefaultComboBoxModel<>(ALGORITHMS));
        comboBoxAlgorithm.setSelectedIndex(0);
    }

    @Override
    JWS performOperation() throws SigningException, UnsupportedKeyException {
        JWSAlgorithm selectedAlgorithm = (JWSAlgorithm) comboBoxAlgorithm.getSelectedItem();

        return Attacks.signWithEmptyKey(jwt, selectedAlgorithm);
    }
}
