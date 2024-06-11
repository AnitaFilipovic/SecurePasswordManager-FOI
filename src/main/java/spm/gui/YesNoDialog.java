package spm.gui;

import javax.swing.*;
import java.awt.event.*;

public class YesNoDialog extends JDialog {
    public static final int RESULT_NO = 0;
    public static final int RESULT_YES = 1;

    private JPanel panel;
    private JButton buttonYes;
    private JButton buttonNo;
    private JLabel labelMessage;

    private int result = RESULT_NO;

    public YesNoDialog(String title, String message) {
        this.setContentPane(panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonYes);

        this.buttonYes.addActionListener(e -> onYes());
        this.buttonNo.addActionListener(e -> onNo());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onNo();
            }
        });

        this.setTitle(title);
        this.labelMessage.setText(message);
    }

    public int getResult() {
        return this.result;
    }

    private void onYes() {
        this.result = YesNoDialog.RESULT_YES;
        dispose();
    }

    private void onNo() {
        this.result = YesNoDialog.RESULT_NO;
        dispose();
    }

    public static YesNoDialog showDialog(String title, String message) {
        YesNoDialog yesNoDialog = new YesNoDialog(title, message);
        yesNoDialog.pack();
        yesNoDialog.setVisible(true);
        return yesNoDialog;
    }
}
