package spm.model;

import spm.storage.PasswordStorageData;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PasswordStorageDataModel extends AbstractTableModel {
    private final List<PasswordStorageData> rows;

    public PasswordStorageDataModel() {
        this.rows = new ArrayList<>();
    }

    public void addRow(PasswordStorageData row) {
        this.rows.add(row);
        int rowIndex = this.rows.size() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void removeRow(PasswordStorageData row) {
        if (this.rows.contains(row)) {
            int rowIndex = this.rows.indexOf(row);
            this.removeRow(rowIndex);
        }
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            this.rows.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void removeAllRows() {
        this.rows.clear();
        this.fireTableDataChanged();
    }

    public PasswordStorageData getRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            return this.rows.get(rowIndex);
        }
        return null;
    }

    public void updateRow(int rowIndex, PasswordStorageData row) {
        if (rowIndex >= 0 && rowIndex < this.rows.size()) {
            this.rows.set(rowIndex, row);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.getRowCount() == 0) {
            return null;
        }
        PasswordStorageData row = this.rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> row.getTitle();
            case 1 -> row.getUsername();
            case 2 -> row.getUrl();
            case 3 -> row.getPort();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Title";
            case 1 -> "Username";
            case 2 -> "URL";
            case 3 -> "Port";
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < 4) {
            return String.class;
        }
        return Object.class;
    }
}
