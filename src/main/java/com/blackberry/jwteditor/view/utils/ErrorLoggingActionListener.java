/*
Author : Dolph Flynn

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

package com.blackberry.jwteditor.view.utils;

import burp.api.montoya.logging.Logging;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

class ErrorLoggingActionListener implements ActionListener {
    private final Logging logging;
    private final ActionListener actionListener;

    ErrorLoggingActionListener(Logging logging, ActionListener actionListener) {
        this.logging = logging;
        this.actionListener = actionListener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            actionListener.actionPerformed(e);
        } catch (RuntimeException ex) {
            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            logging.logToError(stackTrace.toString());
        }
    }
}
