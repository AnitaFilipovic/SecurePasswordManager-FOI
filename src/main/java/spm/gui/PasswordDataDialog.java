package spm.gui;

import spm.storage.PasswordStorageData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordDataDialog extends JDialog {
    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;

    private JPanel panel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldTitle;
    private JPasswordField fieldPassword;
    private JTextField fieldUsername;
    private JTextField fieldUrl;
    private JTextField fieldPort;
    private JButton buttonGenerate;

    private int result = RESULT_CANCEL;

    public PasswordDataDialog(PasswordStorageData passwordStorageData) {
        this.setTitle("Enter password data");
        this.setContentPane(panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> this.onOk());
        buttonCancel.addActionListener(e -> this.onCancel());
        buttonGenerate.addActionListener(e -> this.onGenerate());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        panel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if (null != passwordStorageData) {
            this.fieldTitle.setText(passwordStorageData.getTitle());
            this.fieldUsername.setText(passwordStorageData.getUsername());
            this.fieldPassword.setText(passwordStorageData.getPassword());
            this.fieldUrl.setText(passwordStorageData.getUrl());
            this.fieldPort.setText(passwordStorageData.getPort());
        }
    }

    public int getResult() {
        return this.result;
    }

    public PasswordStorageData getPasswordData() {
        String title = this.fieldTitle.getText();
        String username = this.fieldUsername.getText();
        String password = new String(this.fieldPassword.getPassword());
        String url = this.fieldUrl.getText();
        String port = this.fieldPort.getText();
        return new PasswordStorageData(title, username, password, url, port);
    }

    private void onOk() {
        this.result = RESULT_OK;
        this.dispose();
    }

    private void onCancel() {
        this.result = RESULT_CANCEL;
        this.dispose();
    }

    private void onGenerate() {
        GeneratePasswordDialog generatePasswordDialog = GeneratePasswordDialog.showDialog();
        if (GeneratePasswordDialog.RESULT_OK == generatePasswordDialog.getResult()) {
            String generatedPassword = generatePasswordDialog.getGeneratedPassword();
            this.fieldPassword.setText(generatedPassword);
        }
    }

    public static PasswordDataDialog showDialog() {
        return PasswordDataDialog.showDialog(null);
    }

    public static PasswordDataDialog showDialog(PasswordStorageData passwordStorageData) {
        PasswordDataDialog passwordDataDialog = new PasswordDataDialog(passwordStorageData);
        passwordDataDialog.setPreferredSize(new Dimension(600, 300));
        passwordDataDialog.pack();
        passwordDataDialog.setVisible(true);
        return passwordDataDialog;
    }
}
