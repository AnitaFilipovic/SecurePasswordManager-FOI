package spm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MessageDialog extends JDialog {
    private JPanel panel;
    private JButton buttonClose;
    private JLabel labelMessage;

    public MessageDialog(String title, String message) {
        this.setContentPane(this.panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(this.buttonClose);

        buttonClose.addActionListener(e -> onClose());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
        panel.registerKeyboardAction(e -> onClose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.setTitle(title);
        this.labelMessage.setText(message);
    }

    private void onClose() {
        dispose();
    }

    public static void showDialog(String title, String message) {
        MessageDialog messageDialog = new MessageDialog(title, message);
        messageDialog.pack();
        messageDialog.setVisible(true);
    }
}
