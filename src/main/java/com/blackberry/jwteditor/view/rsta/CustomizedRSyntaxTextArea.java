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

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.function.Consumer;

import static com.blackberry.jwteditor.view.rsta.CustomTokenColors.customTokenColors;
import static java.awt.event.HierarchyEvent.SHOWING_CHANGED;
import static org.fife.ui.rsyntaxtextarea.Theme.load;

class CustomizedRSyntaxTextArea extends RSyntaxTextArea {
    private static final String DARK_THEME = "/org/fife/ui/rsyntaxtextarea/themes/dark.xml";
    private static final String LIGHT_THEME = "/org/fife/ui/rsyntaxtextarea/themes/default.xml";

    private final DarkModeDetector darkModeDetector;
    private final Consumer<String> errorLogger;
    private final CustomTokenColors customTokenColors;

    CustomizedRSyntaxTextArea(DarkModeDetector darkModeDetector, Consumer<String> errorLogger) {
        this(darkModeDetector, errorLogger, customTokenColors().build());
    }

    CustomizedRSyntaxTextArea(DarkModeDetector darkModeDetector, Consumer<String> errorLogger, CustomTokenColors customTokenColors) {
        this.darkModeDetector = darkModeDetector;
        this.errorLogger = errorLogger;
        this.customTokenColors = customTokenColors;

        this.addHierarchyListener(e -> {
            if (e.getChangeFlags() == SHOWING_CHANGED && e.getComponent().isShowing()) {
                applyTheme();
            }
        });

        setUseFocusableTips(false);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        return null;
    }

    @Override
    protected String getToolTipTextImpl(MouseEvent e) {
        return null;
    }

    @Override
    public void setSyntaxEditingStyle(String styleKey) {
        super.setSyntaxEditingStyle(styleKey);
        applyTheme();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        applyTheme();
    }

    @Override
    public Color getForegroundForTokenType(int type) {
        return customTokenColors
                .foregroundForTokenType(type)
                .orElse(super.getForegroundForTokenType(type));
    }

    private void applyTheme() {
        if (errorLogger == null) {
            return;
        }

        String themeResource = darkModeDetector.isDarkMode() ? DARK_THEME : LIGHT_THEME;

        try {
            Theme theme = load(getClass().getResourceAsStream(themeResource));
            theme.apply(this);
        } catch (IOException e) {
            errorLogger.accept(e.getMessage());
        }
    }
}
