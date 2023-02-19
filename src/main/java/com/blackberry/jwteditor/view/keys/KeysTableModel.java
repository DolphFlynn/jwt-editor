package com.blackberry.jwteditor.view.keys;

import com.blackberry.jwteditor.utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;

/**
 * Model for the keys table
 */
public class KeysTableModel extends AbstractTableModel {
    private final List<Object[]> data = new ArrayList<>();

    public void addRow(Object[] row) {
        data.add(row);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return KeysTableColumns.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return KeysTableColumns.values()[column].label;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return KeysTableColumns.values()[columnIndex].type;
    }

    enum KeysTableColumns {
        ID("id", 30, String.class),
        TYPE("type", 10, String.class),
        PUBLIC_KEY("public_key", 10, Boolean.class),
        PRIVATE_KEY("private_key", 10, Boolean.class),
        SIGNING("signing", 10, Boolean.class),
        VERIFICATION("verification", 10, Boolean.class),
        ENCRYPTION("encryption", 10, Boolean.class),
        DECRYPTION("decryption", 10, Boolean.class);

        private final String label;
        private final int widthPercentage;
        private final Class<?> type;

        KeysTableColumns(String labelResourceId, int widthPercentage, Class<?> type) {
            this.label = Utils.getResourceString(labelResourceId);
            this.widthPercentage = widthPercentage;
            this.type = type;
        }

        static int[] columnWidthPercentages() {
            return stream(values()).mapToInt(c -> c.widthPercentage).toArray();
        }
    }
}
