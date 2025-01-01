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

package com.blackberry.jwteditor.view.weak;

import com.blackberry.jwteditor.model.jose.JWS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;

public class WeakKeyAttackDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonAction;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JButton buttonCopy;
    private JTextField textFieldSecret;
    private JLabel labelMessage;

    public WeakKeyAttackDialog(
            Window parent,
            JWS jws) {
        super(parent, "Weak HMAC Secret Attack", APPLICATION_MODAL);

        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonAction);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onAction();
            }
        });

        buttonAction.addActionListener(e -> onAction());
        buttonCopy.setEnabled(false);
        textFieldSecret.setBorder(null);

        contentPane.registerKeyboardAction(
                e -> onAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    private void onAction() {
        setVisible(false);
        dispose();
    }

    public void display() {
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }
}
