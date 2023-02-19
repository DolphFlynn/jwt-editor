package com.blackberry.jwteditor.view.keys;

import com.blackberry.jwteditor.model.keys.Key;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for the keys table
 */
public class KeysTableModel extends AbstractTableModel {
    private final List<Key> data = new ArrayList<>();

    public void addKey(Key key) {
        data.add(key);
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
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }

        Key key = data.get(rowIndex);
        KeysTableColumns columns = KeysTableColumns.fromIndex(columnIndex);

        return switch (columns) {
            case ID -> key.getID();
            case TYPE -> key.getDescription();
            case PUBLIC_KEY -> key.isPublic();
            case PRIVATE_KEY -> key.isPrivate();
            case SIGNING -> key.canSign();
            case VERIFICATION -> key.canVerify();
            case ENCRYPTION -> key.canEncrypt();
            case DECRYPTION -> key.canDecrypt();
        };
    }

    @Override
    public String getColumnName(int column) {
        return KeysTableColumns.values()[column].label();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return KeysTableColumns.values()[columnIndex].type();
    }
}
