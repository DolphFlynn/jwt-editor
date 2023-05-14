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

import javax.swing.*;

import static java.util.Arrays.stream;

public class MaxLengthStringComboBoxModel extends DefaultComboBoxModel<String> {
    private static final String FORMAT_STRING = "%s ...";

    public MaxLengthStringComboBoxModel(int maxLength, String[] items) {
        super(stream(items).map(item -> truncateIfRequired(maxLength, item)).toArray(String[]::new));
    }

    private static String truncateIfRequired(int maxLength, String item) {
        return item != null && item.length() > maxLength
                ? FORMAT_STRING.formatted(item.substring(0, maxLength))
                : item;
    }
}
