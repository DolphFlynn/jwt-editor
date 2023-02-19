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
import javax.swing.table.TableColumnModel;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import static java.awt.event.HierarchyEvent.SHOWING_CHANGED;

public class PercentageBasedColumnWidthTable extends JTable {
    private final int[] columnWidthPercentages;

    public PercentageBasedColumnWidthTable(int[] columnWidthPercentages) {
        this.columnWidthPercentages = columnWidthPercentages;
        addHierarchyListener(new ResizeColumnsOnFirstRenderHierarchyListener());

        tableHeader.setReorderingAllowed(false);
    }

    private void resizeColumns() {
        TableColumnModel columnModel = this.getColumnModel();

        if (columnWidthPercentages == null || columnModel.getColumnCount() != columnWidthPercentages.length) {
            return;
        }

        int tableWidth = getWidth();

        for (int i = 0; i < columnWidthPercentages.length; i++) {
            int preferredWidth = (int) (columnWidthPercentages[i] * 0.01 * tableWidth);
            columnModel.getColumn(i).setPreferredWidth(preferredWidth);
        }
    }

    private class ResizeColumnsOnFirstRenderHierarchyListener implements HierarchyListener {
        @Override
        public void hierarchyChanged(HierarchyEvent e) {
            if (e.getChangeFlags() != SHOWING_CHANGED || !e.getComponent().isShowing()) {
                return;
            }

            resizeColumns();
            removeHierarchyListener(this);
        }
    }
}
