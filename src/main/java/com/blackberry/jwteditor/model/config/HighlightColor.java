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

package com.blackberry.jwteditor.model.config;

import java.awt.*;

import static java.util.Arrays.stream;

/**
 * Enum for highlight colors
 */
public enum HighlightColor {
    RED("Red", "red", Color.RED),
    ORANGE("Orange", "orange", Color.ORANGE),
    YELLOW("Yellow", "yellow", Color.YELLOW),
    GREEN("Green", "green", Color.GREEN),
    CYAN("Cyan", "cyan", Color.CYAN),
    BLUE("Blue", "blue", Color.BLUE),
    PINK("Pink", "pink", Color.PINK),
    MAGENTA("Magenta", "magenta", Color.MAGENTA),
    GRAY("Gray", "gray", Color.GRAY);

    public final String burpColor;
    public final Color color;

    private final String displayName;

    HighlightColor(String displayName, String burpColor, Color color) {
        this.displayName = displayName;
        this.burpColor = burpColor;
        this.color = color;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Factory method to build HighlightColor from Burp's color string
     * @param burpColor Burp's color
     * @return highlight color instance
     */
    public static HighlightColor from(String burpColor) {
        return stream(values())
                .filter(highlightColor -> highlightColor.burpColor.equals(burpColor))
                .findFirst()
                .orElse(null);
    }
}
