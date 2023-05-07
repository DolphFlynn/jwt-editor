/*
Author : Dolph Flynn

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

package com.blackberry.jwteditor.view.config;

import burp.api.montoya.ui.UserInterface;
import burp.config.BurpConfig;
import burp.intruder.FuzzLocation;
import burp.intruder.IntruderConfig;
import burp.proxy.HighlightColor;
import burp.proxy.ProxyConfig;
import burp.scanner.ScannerConfig;
import com.blackberry.jwteditor.view.utils.DocumentAdapter;

import javax.swing.*;
import java.awt.*;

import static java.awt.Font.BOLD;

/**
 *  Config panel
 */
public class ConfigView {
    private JPanel mainPanel;
    private JCheckBox checkBoxHighlightJWT;
    private JLabel labelHighlightColor;
    private JComboBox comboBoxHighlightColor;
    private JLabel labelHighlightJWT;
    private JTextField intruderParameterName;
    private JComboBox comboBoxPayloadPosition;
    private JCheckBox checkBoxHeaderInsertionPoint;
    private JTextField scannerParameterName;
    private JPanel proxyPanel;
    private JLabel proxyLabel;
    private JLabel intruderLabel;
    private JLabel scannerLabel;
    private JPanel intruderPanel;
    private JLabel spacerLabel;

    public ConfigView(BurpConfig burpConfig, UserInterface userInterface) {
        ProxyConfig proxyConfig = burpConfig.proxyConfig();

        checkBoxHighlightJWT.setSelected(proxyConfig.highlightJWT());
        checkBoxHighlightJWT.addActionListener(e -> {
            comboBoxHighlightColor.setEnabled(checkBoxHighlightJWT.isSelected());
            proxyConfig.setHighlightJWT(checkBoxHighlightJWT.isSelected());
        });

        comboBoxHighlightColor.setModel(new DefaultComboBoxModel<>(HighlightColor.values()));
        comboBoxHighlightColor.setSelectedItem(proxyConfig.highlightColor());
        comboBoxHighlightColor.setEnabled(proxyConfig.highlightJWT());
        comboBoxHighlightColor.addActionListener(e -> proxyConfig.setHighlightColor((HighlightColor) comboBoxHighlightColor.getSelectedItem()));

        IntruderConfig intruderConfig = burpConfig.intruderConfig();

        intruderParameterName.setText(intruderConfig.fuzzParameter());
        intruderParameterName.getDocument().addDocumentListener(
                new DocumentAdapter(e -> intruderConfig.setFuzzParameter(intruderParameterName.getText()))
        );

        comboBoxPayloadPosition.setModel(new DefaultComboBoxModel<>(FuzzLocation.values()));
        comboBoxPayloadPosition.setSelectedItem(intruderConfig.fuzzLocation());
        comboBoxPayloadPosition.addActionListener(e -> intruderConfig.setFuzzLocation((FuzzLocation) comboBoxPayloadPosition.getSelectedItem()));

        ScannerConfig scannerConfig = burpConfig.scannerConfig();

        checkBoxHeaderInsertionPoint.setSelected(scannerConfig.enableHeaderJWSInsertionPointLocation());
        checkBoxHeaderInsertionPoint.addActionListener(e -> {
            scannerConfig.setEnableHeaderJWSInsertionPointLocation(checkBoxHeaderInsertionPoint.isSelected());
            scannerParameterName.setEnabled(checkBoxHeaderInsertionPoint.isSelected());
        });

        scannerParameterName.setEnabled(scannerConfig.enableHeaderJWSInsertionPointLocation());
        scannerParameterName.setText(scannerConfig.insertionPointLocationParameterName());
        scannerParameterName.getDocument().addDocumentListener(
                new DocumentAdapter(e -> scannerConfig.setInsertionPointLocationParameterName(scannerParameterName.getText()))
        );

        proxyLabel.setFont(proxyLabel.getFont().deriveFont(BOLD));
        intruderLabel.setFont(intruderLabel.getFont().deriveFont(BOLD));
        scannerLabel.setFont(scannerLabel.getFont().deriveFont(BOLD));
        userInterface.applyThemeToComponent(mainPanel);

        comboBoxHighlightColor.setRenderer(new HighlightComboRenderer());
    }

    /**
     * Custom list cell renderer to color rows of combo box drop down list.
     */
    private static class HighlightComboRenderer implements ListCellRenderer<HighlightColor> {
        private final ListCellRenderer renderer = new DefaultListCellRenderer();

        @Override
        public Component getListCellRendererComponent(JList<? extends HighlightColor> list, HighlightColor value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            Color background = isSelected ? list.getSelectionBackground() : value.color;
            label.setBackground(background);

            return label;
        }
    }
}
