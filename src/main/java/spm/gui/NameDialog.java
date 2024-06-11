package spm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NameDialog extends JDialog {
    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;

    private JPanel panel;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JLabel labelError;

    private int result = RESULT_CANCEL;

    public NameDialog(String title) {
        this.setTitle(title);
        this.setContentPane(panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonOk);

        this.buttonOk.addActionListener(e -> this.onOk());
        this.buttonCancel.addActionListener(e -> this.onCancel());

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        this.panel.registerKeyboardAction(e -> this.onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    public int getResult() {
        return this.result;
    }

    public String getName() {
        return this.fieldName.getText();
    }

    private void onOk() {
        this.labelError.setText("");

        if (this.fieldName.getText().isEmpty()) {
            this.labelError.setText("Name must not be empty.");
            return;
        }

        this.result = RESULT_OK;
        dispose();
    }

    private void onCancel() {
        this.result = RESULT_CANCEL;
        dispose();
    }

    public static NameDialog showDialog(String title) {
        NameDialog nameDialog = new NameDialog(title);
        nameDialog.setPreferredSize(new Dimension(600, 120));
        nameDialog.pack();
        nameDialog.setVisible(true);
        return nameDialog;
    }

    public static NameDialog showDialogForFolder() {
        return NameDialog.showDialog("Enter folder name");
    }

    public static NameDialog showDialogForItem() {
        return NameDialog.showDialog("Enter item name");
    }
}
