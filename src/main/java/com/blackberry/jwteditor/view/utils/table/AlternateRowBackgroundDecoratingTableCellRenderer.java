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

package com.blackberry.jwteditor.view.utils.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public record AlternateRowBackgroundDecoratingTableCellRenderer(TableCellRenderer tableCellRenderer) implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected && !hasFocus) {
            Color alternateRowColor = UIManager.getColor("Table.alternateRowColor");

            if (alternateRowColor != null && row % 2 != 0) {
                component.setBackground(alternateRowColor);
            }
        }

        return component;
    }
}
