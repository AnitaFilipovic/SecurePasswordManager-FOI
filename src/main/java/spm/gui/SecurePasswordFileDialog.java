package spm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class SecurePasswordFileDialog extends JDialog {
    public static final int RESULT_CANCEL = 0;
    public static final int RESULT_OK = 1;

    public static final int MODE_OPEN = 0;
    public static final int MODE_SAVE = 1;

    private JPanel panel;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JPasswordField fieldPassword;
    private JTextField fieldPath;
    private JButton buttonBrowse;
    private JLabel labelError;

    private int result = RESULT_CANCEL;
    private final int mode;

    private SecurePasswordFileDialog(int mode) {
        this.mode = mode;
        this.setContentPane(panel);
        this.setModal(true);
        this.getRootPane().setDefaultButton(buttonOk);

        this.buttonOk.addActionListener(e -> this.onOk());
        this.buttonCancel.addActionListener(e -> this.onCancel());
        this.buttonBrowse.addActionListener(e -> this.onBrowse());

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { onCancel(); }
        });

        this.panel.registerKeyboardAction(e -> this.onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    public int getResult() {
        return this.result;
    }

    public String getFilePath() {
        return this.fieldPath.getText();
    }

    public String getPassword() {
        return Arrays.toString(this.fieldPassword.getPassword());
    }

    private void onOk() {
        this.labelError.setText("");

        if (this.fieldPassword.getPassword().length == 0) {
            this.labelError.setText("Password must not be empty.");
            return;
        }

        if (!this.isValidPath(this.getFilePath())) {
            this.labelError.setText("Invalid file path.");
            return;
        }

        this.result = RESULT_OK;
        this.dispose();
    }

    private void onCancel() {
        this.result = RESULT_CANCEL;
        this.dispose();
    }

    private void onBrowse() {
        FileDialog fileDialog = this.createFileDialog();

        String filename = fileDialog.getFile();
        if (null != filename && !filename.isEmpty()) {
            String folder = fileDialog.getDirectory();
            String path = Path.of(folder, filename).toAbsolutePath().toString();

            if (!path.endsWith(".spm")) {
                path += ".spm";
            }

            this.fieldPath.setText(path);
        }
    }

    private FileDialog createFileDialog() {
        FileDialog fileDialog = new FileDialog(this);

        if (this.mode == MODE_SAVE) {
            fileDialog.setTitle("Create new password storage file");
            fileDialog.setMode(FileDialog.SAVE);
            fileDialog.setFile("NewPasswordFile.spm");
        } else {
            fileDialog.setTitle("Open password storage file");
            fileDialog.setMode(FileDialog.LOAD);
            fileDialog.setFile("*.spm");
        }

        fileDialog.setVisible(true);
        return fileDialog;
    }

    private boolean isValidPath(String path) {
        if (path.isEmpty()) {
            return false;
        }

        try {
            Paths.get(path);
        } catch (InvalidPathException e) {
            return false;
        }

        return true;
    }

    public static SecurePasswordFileDialog showDialog(int mode) {
        SecurePasswordFileDialog securePasswordFileDialog = new SecurePasswordFileDialog(mode);
        securePasswordFileDialog.setPreferredSize(new Dimension(600, 160));
        securePasswordFileDialog.pack();
        securePasswordFileDialog.setVisible(true);
        return securePasswordFileDialog;
    }

    public static SecurePasswordFileDialog showOpenDialog() {
        return SecurePasswordFileDialog.showDialog(MODE_OPEN);
    }

    public static SecurePasswordFileDialog showSaveDialog() {
        return SecurePasswordFileDialog.showDialog(MODE_SAVE);
    }

}
