package spm.listener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RightClickSelectTableMouseListener extends MouseAdapter {
    private final JTable table;

    public RightClickSelectTableMouseListener(JTable table) {
        this.table = table;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!SwingUtilities.isRightMouseButton(e)) {
            return;
        }

        int selectedRow = this.table.rowAtPoint(e.getPoint());
        if (selectedRow >= 0) {
            this.table.clearSelection();
            this.table.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }
}
