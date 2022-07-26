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

import java.awt.*;
import java.util.stream.Stream;

/**
 * Utility class to find Window with name
 */
public class WindowUtils {

    private WindowUtils() {
    }

    /**
     * Utility method to find Window with specified name
     *
     * @param windowName name of the window to find
     * @return named window or null
     */
    public static Window findWindowWithName(String windowName) {
        return Stream.of(Frame.getFrames())
                .filter(f -> windowName.equals(f.getName()))
                .findFirst().orElse(null);
    }
}
