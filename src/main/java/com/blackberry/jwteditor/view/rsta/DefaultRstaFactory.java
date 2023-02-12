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

package com.blackberry.jwteditor.view.rsta;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.UserInterface;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class DefaultRstaFactory implements RstaFactory {
    private final DarkModeDetector darkModeDetector;
    private final Logging logging;

    public DefaultRstaFactory(UserInterface userInterface, Logging logging) {
        this.darkModeDetector = new DarkModeDetector(userInterface);
        this.logging = logging;

        // Ensure Burp key events not captured - https://github.com/bobbylight/RSyntaxTextArea/issues/269#issuecomment-776329702
        JTextComponent.removeKeymap("RTextAreaKeymap");
        UIManager.put("RSyntaxTextAreaUI.actionMap", null);
        UIManager.put("RSyntaxTextAreaUI.inputMap", null);
        UIManager.put("RTextAreaUI.actionMap", null);
        UIManager.put("RTextAreaUI.inputMap", null);
    }

    @Override
    public RSyntaxTextArea build() {
        RSyntaxTextArea textArea = new CustomizedRSyntaxTextArea(darkModeDetector, logging::logToError);
        textArea.setUseFocusableTips(false);

        return textArea;
    }
}
