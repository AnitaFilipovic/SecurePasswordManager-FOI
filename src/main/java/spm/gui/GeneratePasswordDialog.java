package spm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.PrivilegedAction;
import java.security.SecureRandom;

public class GeneratePasswordDialog extends JDialog {
    public static int RESULT_CANCEL = 0;
    public static int RESULT_OK = 1;

    private JPanel panel;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JTextField fieldGeneratedPassword;
    private JSlider sliderPasswordLength;
    private JCheckBox checkIncludeLowercase;
    private JCheckBox checkIncludeUppercase;
    private JCheckBox checkIncludeNumbers;
    private JCheckBox checkIncludeSymbols;
    private JButton buttonGenerate;
    private JLabel labelPasswordLength;

    private final String lowercaseCharacters = "abcdefghijklmnopqrstuvwxyz";
    private final String uppercaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String numbers = "0123456789";
    private final String symbols = "()[]{}$%!?+-*/,.:,;-_";

    private int result = RESULT_CANCEL;

    public GeneratePasswordDialog() {
        this.setTitle("Generate password");
        this.setContentPane(panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonOk);

        this.buttonOk.addActionListener(e -> this.onOk());
        this.buttonCancel.addActionListener(e -> this.onCancel());
        this.buttonGenerate.addActionListener(e -> this.onGenerate());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        this.panel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        this.sliderPasswordLength.addChangeListener(e -> this.onPasswordLengthChange());
        this.labelPasswordLength.setText(String.valueOf(this.sliderPasswordLength.getValue()));
    }

    public String getGeneratedPassword() {
        return this.fieldGeneratedPassword.getText();
    }

    public int getResult() {
        return this.result;
    }

    private void onOk() {
        this.result = RESULT_OK;
        dispose();
    }

    private void onCancel() {
        this.result = RESULT_CANCEL;
        dispose();
    }

    private void onGenerate() {
        int passwordLength = this.sliderPasswordLength.getValue();
        String passwordCharacters = "";
        if (this.checkIncludeLowercase.isSelected()) {
            passwordCharacters += this.lowercaseCharacters;
        }
        if (this.checkIncludeUppercase.isSelected()) {
            passwordCharacters += this.uppercaseCharacters;
        }
        if (this.checkIncludeNumbers.isSelected()) {
            passwordCharacters += this.numbers;
        }
        if (this.checkIncludeSymbols.isSelected()) {
            passwordCharacters += this.symbols;
        }

        if (passwordCharacters.isEmpty()) {
            this.fieldGeneratedPassword.setText("");
            return;
        }

        long timestamp = System.currentTimeMillis();
        byte[] seed = String.valueOf(timestamp).getBytes();
        SecureRandom secureRandom = new SecureRandom(seed);

        StringBuilder generatedPassword = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            int passwordIndex = secureRandom.nextInt(0, passwordCharacters.length());
            char passwordCharacter = passwordCharacters.charAt(passwordIndex);
            generatedPassword.append(passwordCharacter);
        }

        this.fieldGeneratedPassword.setText(generatedPassword.toString());
    }

    private void onPasswordLengthChange() {
        this.labelPasswordLength.setText(String.valueOf(this.sliderPasswordLength.getValue()));
    }

    public static GeneratePasswordDialog showDialog() {
        GeneratePasswordDialog generatePasswordDialog = new GeneratePasswordDialog();
        generatePasswordDialog.setPreferredSize(new Dimension(600, 300));
        generatePasswordDialog.pack();
        generatePasswordDialog.setVisible(true);
        return generatePasswordDialog;
    }
}
