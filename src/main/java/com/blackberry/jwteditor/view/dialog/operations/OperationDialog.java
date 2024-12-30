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
import com.blackberry.jwteditor.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

public abstract class OperationDialog<T> extends JDialog {
    private final String errorTitleOperationFailed;
    private final Logging logging;

    T jwt;

    OperationDialog(Window parent, Logging logging, String titleResourceId, T jwt) {
        this(parent, logging, titleResourceId, jwt, "error_title_unable_to_perform_operation");
    }

    OperationDialog(Window parent, Logging logging, String titleResourceId, T jwt, String errorTitleOperationFailed) {
        super(parent, Utils.getResourceString(titleResourceId), APPLICATION_MODAL);

        this.logging = logging;
        this.jwt = jwt;
        this.errorTitleOperationFailed = errorTitleOperationFailed;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    void configureUI(JPanel contentPane, JButton buttonOK, JButton buttonCancel) {
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
    }

    abstract T performOperation() throws Exception;

    public T getJWT() {
        return jwt;
    }

    public void display() {
        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    void onOK() {
        try {
            jwt = performOperation();
        } catch (Exception e) {
            String title = Utils.getResourceString(errorTitleOperationFailed);
            logging.logToError(title, e);
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    title,
                    WARNING_MESSAGE
            );
            jwt = null;
        } finally {
            dispose();
        }
    }

    void onCancel() {
        dispose();
    }
}
