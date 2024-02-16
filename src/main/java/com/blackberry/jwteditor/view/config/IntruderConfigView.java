/*
Author : Dolph Flynn

Copyright 2024 Dolph Flynn

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

package com.blackberry.jwteditor.view.config;

import burp.api.montoya.ui.UserInterface;
import burp.intruder.FuzzLocation;
import burp.intruder.IntruderConfig;
import com.blackberry.jwteditor.model.keys.Key;
import com.blackberry.jwteditor.model.keys.KeysModel;
import com.blackberry.jwteditor.model.keys.KeysModelListener.SimpleKeysModelListener;
import com.blackberry.jwteditor.view.utils.DocumentAdapter;
import com.nimbusds.jose.JWSAlgorithm;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static java.awt.Font.BOLD;


class IntruderConfigView {
    private final IntruderConfig intruderConfig;
    private final KeysModel keysModel;

    private JPanel mainPanel;
    private JTextField intruderParameterName;
    private JComboBox comboBoxPayloadPosition;
    private JComboBox comboBoxIntruderSigningKeyId;
    private JLabel intruderLabel;
    private JLabel spacerLabel;
    private JCheckBox resignIntruderJWS;
    private JComboBox comboBoxIntruderSigningAlg;

    IntruderConfigView(UserInterface userInterface, KeysModel keysModel, IntruderConfig intruderConfig) {
        this.keysModel = keysModel;
        this.intruderConfig = intruderConfig;

        intruderParameterName.setText(intruderConfig.fuzzParameter());
        intruderParameterName.getDocument().addDocumentListener(
                new DocumentAdapter(e -> intruderConfig.setFuzzParameter(intruderParameterName.getText()))
        );

        comboBoxPayloadPosition.setModel(new DefaultComboBoxModel<>(FuzzLocation.values()));
        comboBoxPayloadPosition.setSelectedItem(intruderConfig.fuzzLocation());
        comboBoxPayloadPosition.addActionListener(e -> intruderConfig.setFuzzLocation((FuzzLocation) comboBoxPayloadPosition.getSelectedItem()));

        updateSigningKeyList();
        comboBoxIntruderSigningKeyId.addActionListener(e -> {
            String newSigningKeyId = (String) comboBoxIntruderSigningKeyId.getSelectedItem();

            if (!intruderConfig.signingKeyId().equals(newSigningKeyId)) {
                intruderConfig.setSigningKeyId(newSigningKeyId);
                updateSigningAlgorithmList();
            }
        });
        comboBoxIntruderSigningAlg.addActionListener(e -> intruderConfig.setSigningAlgorithm((JWSAlgorithm) comboBoxIntruderSigningAlg.getSelectedItem()));
        resignIntruderJWS.addActionListener(e -> intruderConfig.setResign(resignIntruderJWS.isSelected()));
        keysModel.addKeyModelListener(new SimpleKeysModelListener(this::updateSigningKeyList));

        intruderLabel.setFont(intruderLabel.getFont().deriveFont(BOLD));
        userInterface.applyThemeToComponent(mainPanel);
    }

    private void updateSigningKeyList() {
        List<Key> signingKeys = keysModel.getSigningKeys();
        String[] signingKeyIds = signingKeys.stream().map(Key::getID).toArray(String[]::new);
        String modelSelectedSigningId = intruderConfig.signingKeyId();

        String viewSelectedKeyId = (String) comboBoxIntruderSigningKeyId.getSelectedItem();
        comboBoxIntruderSigningKeyId.setModel(new DefaultComboBoxModel<>(signingKeyIds));

        if (signingKeys.isEmpty()) {
            resignIntruderJWS.setSelected(false);
            resignIntruderJWS.setEnabled(false);
            comboBoxIntruderSigningKeyId.setEnabled(false);
            comboBoxIntruderSigningAlg.setEnabled(false);
            intruderConfig.setResign(false);
            intruderConfig.setSigningKeyId(null);
        } else {
            resignIntruderJWS.setEnabled(true);
            comboBoxIntruderSigningKeyId.setEnabled(true);
            comboBoxIntruderSigningAlg.setEnabled(true);

            Optional<Key> selectedKey = signingKeys.stream()
                    .filter(k -> k.getID().equals(modelSelectedSigningId))
                    .findFirst();

            if (selectedKey.isPresent()) {
                Key key = selectedKey.get();

                resignIntruderJWS.setSelected(intruderConfig.resign());
                comboBoxIntruderSigningKeyId.setSelectedItem(key.getID());

                if (!modelSelectedSigningId.equals(viewSelectedKeyId)) {
                    comboBoxIntruderSigningAlg.setModel(new DefaultComboBoxModel(key.getSigningAlgorithms()));
                    comboBoxIntruderSigningAlg.setSelectedIndex(0);
                }
            } else {
                resignIntruderJWS.setSelected(false);
                comboBoxIntruderSigningKeyId.setSelectedIndex(0);

                Key key = signingKeys.get(0);
                comboBoxIntruderSigningAlg.setModel(new DefaultComboBoxModel(key.getSigningAlgorithms()));
            }
        }
    }

    private void updateSigningAlgorithmList() {
        Key key = keysModel.getSigningKeys().stream()
                .filter(k -> k.getID().equals(intruderConfig.signingKeyId()))
                .findFirst()
                .orElseThrow();

        JWSAlgorithm[] signingAlgorithms = key.getSigningAlgorithms();
        comboBoxIntruderSigningAlg.setModel(new DefaultComboBoxModel(signingAlgorithms));

        if (signingAlgorithms.length > 0) {
            JWSAlgorithm algorithm = signingAlgorithms[0];
            comboBoxIntruderSigningAlg.setSelectedItem(algorithm);
            intruderConfig.setSigningAlgorithm(algorithm);
        }
    }
}
