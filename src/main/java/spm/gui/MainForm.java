package spm.gui;

import org.xml.sax.SAXException;
import spm.encdec.EncDec;
import spm.storage.PasswordStorageFolder;
import spm.storage.PasswordStorageItem;
import spm.storage.PasswordStorageRoot;
import spm.storage.PasswordStorageXmlMapper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import java.awt.*;
import java.io.*;

public class MainForm {
    private JPanel panel;
    private JButton buttonNew;
    private JButton buttonOpen;
    private JButton buttonSave;
    private JButton buttonAddFolder;
    private JButton buttonDeleteFolder;
    private JButton buttonAddItem;
    private JButton buttonDeleteItem;
    private JButton buttonAddData;
    private PasswordStorageForm passwordStorageForm;

    private boolean addedTreeSelectionListener = false;

    public MainForm() {
        this.buttonNew.addActionListener(e -> this.onNew());
        this.buttonOpen.addActionListener(e -> this.onOpen());
        this.buttonSave.addActionListener(e -> this.onSave());
        this.buttonAddFolder.addActionListener(e -> this.onAddFolder());
        this.buttonDeleteFolder.addActionListener(e -> this.onDeleteFolder());
        this.buttonAddItem.addActionListener(e -> this.onAddItem());
        this.buttonDeleteItem.addActionListener(e -> this.onDeleteItem());
        this.buttonAddData.addActionListener(e -> this.onAddData());

        this.buttonNew.setName("buttonNew");
    }

    private void onNew() {
        SecurePasswordFileDialog securePasswordFileDialog = SecurePasswordFileDialog.showSaveDialog();
        if (SecurePasswordFileDialog.RESULT_OK == securePasswordFileDialog.getResult()) {
            String filepath = securePasswordFileDialog.getFilePath();
            String password = securePasswordFileDialog.getPassword();
            createPasswordStorage(filepath, password);
            openPasswordStorage(filepath, password);
        }
    }

    private void onOpen() {
        SecurePasswordFileDialog securePasswordFileDialog = SecurePasswordFileDialog.showOpenDialog();
        if (SecurePasswordFileDialog.RESULT_OK == securePasswordFileDialog.getResult()) {
            String filepath = securePasswordFileDialog.getFilePath();
            String password = securePasswordFileDialog.getPassword();
            openPasswordStorage(filepath, password);
        }
    }

    private void onAddFolder() {
        this.passwordStorageForm.addFolder();
    }

    private void onDeleteFolder() {
        YesNoDialog yesNoDialog = YesNoDialog.showDialog("Delete folder", "Are you sure you want to delete this folder?");
        if (YesNoDialog.RESULT_YES == yesNoDialog.getResult()) {
            this.passwordStorageForm.deleteFolder();
        }
    }

    private void onAddItem() {
        this.passwordStorageForm.addItem();
    }

    private void onDeleteItem() {
        YesNoDialog yesNoDialog = YesNoDialog.showDialog("Delete item", "Are you sure you want to delete this item?");
        if (YesNoDialog.RESULT_YES == yesNoDialog.getResult()) {
            this.passwordStorageForm.deleteItem();
        }
    }

    private void onAddData() {
        this.passwordStorageForm.addData();
    }

    private void onSave() {
        this.passwordStorageForm.save();
    }

    private void createPasswordStorage(String filepath, String password) {
        try {
            PasswordStorageRoot passwordStorageRoot = new PasswordStorageRoot();

            EncDec encDec = new EncDec(password);
            byte[] encrypted = encDec.encrypt(PasswordStorageXmlMapper.toString(passwordStorageRoot));
            try (FileOutputStream output = new FileOutputStream(filepath)) {
                output.write(encrypted);
            }
        } catch (ParserConfigurationException | IllegalBlockSizeException | BadPaddingException | TransformerException | IOException e) {
            MessageDialog.showDialog("Error creating password storage", "An error occurred while trying to create a password storage.");
        }
    }

    private void openPasswordStorage(String filepath, String password) {
        try {
            byte[] encryptedStorage;
            try (FileInputStream input = new FileInputStream(filepath)) {
                encryptedStorage = input.readAllBytes();
            }
            EncDec encDec = new EncDec(password);
            String decrypted = encDec.decrypt(encryptedStorage);
            PasswordStorageRoot passwordStorageRoot = PasswordStorageXmlMapper.fromXml(decrypted);

            this.passwordStorageForm.setRoot(passwordStorageRoot);
            this.passwordStorageForm.setEncDec(encDec);
            this.passwordStorageForm.setFilepath(filepath);

            if (!this.addedTreeSelectionListener) {
                this.passwordStorageForm.addTreeSelectionListener(e -> {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
                    if (null == selectedNode) {
                        this.buttonAddItem.setEnabled(false);
                        return;
                    }

                    if (selectedNode.getUserObject() instanceof PasswordStorageFolder) {
                        this.buttonDeleteFolder.setEnabled(true);
                        this.buttonAddItem.setEnabled(true);
                        this.buttonDeleteItem.setEnabled(false);
                        this.buttonAddData.setEnabled(false);
                    } else if (selectedNode.getUserObject() instanceof PasswordStorageItem) {
                        this.buttonDeleteFolder.setEnabled(false);
                        this.buttonAddItem.setEnabled(false);
                        this.buttonDeleteItem.setEnabled(true);
                        this.buttonAddData.setEnabled(true);
                    } else {
                        this.buttonDeleteFolder.setEnabled(false);
                        this.buttonAddItem.setEnabled(false);
                        this.buttonDeleteItem.setEnabled(false);
                        this.buttonAddData.setEnabled(false);
                    }
                });
                this.addedTreeSelectionListener = true;
            }

            this.buttonSave.setEnabled(true);
            this.buttonAddFolder.setEnabled(true);
        } catch (IOException | ParserConfigurationException | IllegalBlockSizeException | BadPaddingException | SAXException e) {
            MessageDialog.showDialog("Error opening password storage", "An error occurred while trying to open the password storage.");
        }
    }

    public static JFrame run() {
        JFrame frame = new JFrame("Secure Password Manager");
        frame.setContentPane(new MainForm().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
}
