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

package com.blackberry.jwteditor.view.utils;

import javax.swing.*;

/**
 * Utility class to detect Burp's current theme
 */
public class ThemeDetector {
    private static final String DARK_THEME_NAME = "Burp Dark";

    private ThemeDetector() {
    }

    /**
     * Used to detect Burp's current theme
     *
     * @return true if current Burp theme is light
     */
    public static boolean isLightTheme() {
        return !DARK_THEME_NAME.equals(UIManager.getLookAndFeel().getName());
    }
}