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

package burp.proxy;

import java.awt.*;

import static java.util.Arrays.stream;

/**
 * Enum for highlight colors
 */
public enum HighlightColor {
    RED("Red", burp.api.montoya.core.HighlightColor.RED, Color.RED),
    ORANGE("Orange", burp.api.montoya.core.HighlightColor.ORANGE, Color.ORANGE),
    YELLOW("Yellow", burp.api.montoya.core.HighlightColor.YELLOW, Color.YELLOW),
    GREEN("Green", burp.api.montoya.core.HighlightColor.GREEN, Color.GREEN),
    CYAN("Cyan", burp.api.montoya.core.HighlightColor.CYAN, Color.CYAN),
    BLUE("Blue", burp.api.montoya.core.HighlightColor.BLUE, Color.BLUE),
    PINK("Pink", burp.api.montoya.core.HighlightColor.PINK, Color.PINK),
    MAGENTA("Magenta", burp.api.montoya.core.HighlightColor.MAGENTA, Color.MAGENTA),
    GRAY("Gray", burp.api.montoya.core.HighlightColor.GRAY, Color.GRAY);

    public final burp.api.montoya.core.HighlightColor burpColor;
    public final Color color;

    private final String displayName;

    HighlightColor(String displayName, burp.api.montoya.core.HighlightColor burpColor, Color color) {
        this.displayName = displayName;
        this.burpColor = burpColor;
        this.color = color;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Factory method to build HighlightColor from display name string
     * @param displayName Color's display name
     * @return highlight color instance
     */
    public static HighlightColor from(String displayName) {
        return stream(values())
                .filter(highlightColor -> highlightColor.displayName.equalsIgnoreCase(displayName))
                .findFirst()
                .orElse(null);
    }
}
