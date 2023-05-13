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

package com.blackberry.jwteditor.view.hexcodearea;

import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.UserInterface;
import com.blackberry.jwteditor.view.utils.FontProvider;
import org.exbin.deltahex.swing.CodeArea;
import org.exbin.utils.binary_data.ByteArrayEditableData;

import static org.exbin.deltahex.ViewMode.CODE_MATRIX;

public class HexCodeAreaFactory {
    private final Logging logging;
    private final FontProvider fontProvider;

    public HexCodeAreaFactory(Logging logging, UserInterface userInterface) {
        this.logging = logging;
        this.fontProvider = new FontProvider(userInterface);
    }

    public CodeArea build() {
        CodeArea codeArea = new FontMetricsClearingCodeArea(logging);

        codeArea.setCommandHandler(new HexCodeAreaCommandHandler(codeArea));
        codeArea.setShowHeader(false);
        codeArea.setShowLineNumbers(false);
        codeArea.setViewMode(CODE_MATRIX);
        codeArea.setData(new ByteArrayEditableData());
        codeArea.setFont(fontProvider.editorFont());

        return codeArea;
    }
}
