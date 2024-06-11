package spm.gui;

import spm.encdec.EncDec;
import spm.listener.RightClickSelectTableMouseListener;
import spm.model.PasswordStorageDataModel;
import spm.storage.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.FileOutputStream;
import java.io.IOException;

public class PasswordStorageForm {
    private JPanel panel;
    private JTree storageTree;
    private JTable passwordTable;

    private PasswordStorageRoot root;
    private EncDec encDec;
    private String filepath;

    private DefaultMutableTreeNode selectedNode;
    private PasswordStorageItem selectedItem;
    private PasswordStorageDataModel tableModel;

    public PasswordStorageForm() {
        this.storageTree.addTreeSelectionListener(e -> {
            this.selectedNode = (DefaultMutableTreeNode) storageTree.getLastSelectedPathComponent();
            if (null != this.selectedNode) {
                Object userObject = this.selectedNode.getUserObject();
                if (userObject instanceof PasswordStorageItem) {
                    this.selectedItem = (PasswordStorageItem) userObject;
                } else {
                    this.selectedItem = null;
                }
            }
            this.updatePasswordTable();
        });
    }

    public void setRoot(PasswordStorageRoot root) {
        DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode(root);
        for (PasswordStorageFolder folder : root) {
            DefaultMutableTreeNode nodeFolder = new DefaultMutableTreeNode(folder);
            for (PasswordStorageItem data : folder) {
                DefaultMutableTreeNode nodeData = new DefaultMutableTreeNode(data);
                nodeData.setAllowsChildren(false);
                nodeFolder.add(nodeData);
            }
            nodeRoot.add(nodeFolder);
        }
        this.storageTree.setModel(new DefaultTreeModel(nodeRoot));
        this.root = root;
        this.panel.setVisible(true);

        this.passwordTable.clearSelection();
        this.storageTree.clearSelection();
        this.selectedNode = null;
        this.selectedItem = null;
        this.updatePasswordTable();
    }

    public void setEncDec(EncDec encDec) {
        this.encDec = encDec;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void addTreeSelectionListener(TreeSelectionListener treeSelectionListener) {
        this.storageTree.addTreeSelectionListener(treeSelectionListener);
    }

    public void addFolder() {
        NameDialog nameDialog = NameDialog.showDialogForFolder();
        if (NameDialog.RESULT_OK == nameDialog.getResult()) {
            String name = nameDialog.getName();
            PasswordStorageFolder folder = new PasswordStorageFolder(name);
            this.root.add(folder);

            DefaultTreeModel model = (DefaultTreeModel) this.storageTree.getModel();
            DefaultMutableTreeNode nodeRoot = (DefaultMutableTreeNode) model.getRoot();
            nodeRoot.add(new DefaultMutableTreeNode(folder));
            model.reload(nodeRoot);
        }
    }

    public void deleteFolder() {
        if (null == this.selectedNode || !(this.selectedNode.getUserObject() instanceof PasswordStorageFolder)) {
            return;
        }

        DefaultMutableTreeNode currentNode = this.selectedNode;
        PasswordStorageFolder folder = (PasswordStorageFolder) currentNode.getUserObject();
        DefaultTreeModel model = (DefaultTreeModel) this.storageTree.getModel();
        DefaultMutableTreeNode nodeRoot = (DefaultMutableTreeNode) model.getRoot();
        this.storageTree.clearSelection();

        this.root.remove(folder);
        nodeRoot.remove(currentNode);
        model.reload(nodeRoot);
    }

    public void addItem() {
        NameDialog nameDialog = NameDialog.showDialogForItem();
        if (NameDialog.RESULT_OK == nameDialog.getResult() && this.selectedNode != null && this.selectedNode.getUserObject() instanceof PasswordStorageFolder) {
            String name = nameDialog.getName();
            PasswordStorageItem item = new PasswordStorageItem(name);
            PasswordStorageFolder folder = (PasswordStorageFolder) this.selectedNode.getUserObject();
            folder.add(item);

            this.selectedNode.add(new DefaultMutableTreeNode(item));
            DefaultTreeModel model = (DefaultTreeModel) this.storageTree.getModel();
            model.reload(this.selectedNode);
        }
    }

    public void deleteItem() {
        if (null == this.selectedNode || !(this.selectedNode.getUserObject() instanceof PasswordStorageItem)) {
            return;
        }

        DefaultMutableTreeNode currentNode = this.selectedNode;
        PasswordStorageItem item = (PasswordStorageItem) currentNode.getUserObject();
        DefaultTreeModel model = (DefaultTreeModel) this.storageTree.getModel();
        DefaultMutableTreeNode nodeRoot = (DefaultMutableTreeNode) model.getRoot();
        this.storageTree.clearSelection();

        for (PasswordStorageFolder folder : this.root) {
            if (folder.contains(item)) {
                folder.remove(item);
                break;
            }
        }

        DefaultMutableTreeNode nodeFolder = (DefaultMutableTreeNode) currentNode.getParent();
        nodeFolder.remove(currentNode);
        model.reload(nodeFolder);
    }

    public void addData() {
        if (null == this.selectedItem) {
            return;
        }

        PasswordDataDialog passwordDataDialog = PasswordDataDialog.showDialog();
        if (PasswordDataDialog.RESULT_OK == passwordDataDialog.getResult()) {
            PasswordStorageData passwordStorageData = passwordDataDialog.getPasswordData();
            this.selectedItem.add(passwordStorageData);
            this.updatePasswordTable();
        }
    }

    public void save() {
        if (null == this.encDec || null == this.filepath || this.filepath.isEmpty()) {
            return;
        }

        try {
            byte[] encrypted = encDec.encrypt(PasswordStorageXmlMapper.toString(this.root));
            try (FileOutputStream output = new FileOutputStream(filepath)) {
                output.write(encrypted);
            }
        } catch (IllegalBlockSizeException | BadPaddingException | ParserConfigurationException | TransformerException | IOException e) {
            MessageDialog.showDialog("Error saving password storage", "An error occurred while trying to open the password storage.");
        }
    }

    private void updatePasswordTable() {
        this.tableModel.removeAllRows();
        if  (null != this.selectedItem) {
            for (PasswordStorageData data : this.selectedItem) {
                this.tableModel.addRow(data);
            }
        }
    }

    private void onCopyUsername() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0) {
            PasswordStorageData data = this.tableModel.getRow(selectedRow);
            StringSelection ss = new StringSelection(data.getUsername());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        }
    }

    private void onCopyPassword() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0) {
            PasswordStorageData data = this.tableModel.getRow(selectedRow);
            StringSelection ss = new StringSelection(data.getPassword());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        }
    }

    private void onCopyUrl() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0) {
            PasswordStorageData data = this.tableModel.getRow(selectedRow);
            StringSelection ss = new StringSelection(data.getUrl());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        }
    }

    private void onCopyPort() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0) {
            PasswordStorageData data = this.tableModel.getRow(selectedRow);
            StringSelection ss = new StringSelection(data.getPort());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        }
    }

    private void onEditPassword() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < this.passwordTable.getRowCount() && null != this.selectedItem) {
            PasswordStorageData data = this.tableModel.getRow(selectedRow);
            PasswordDataDialog passwordDataDialog = PasswordDataDialog.showDialog(data);
            if (PasswordDataDialog.RESULT_OK == passwordDataDialog.getResult()) {
                PasswordStorageData passwordStorageData = passwordDataDialog.getPasswordData();
                this.selectedItem.set(selectedRow, passwordStorageData);
                this.updatePasswordTable();
            }
        }
    }

    private void onDeletePassword() {
        int selectedRow = this.passwordTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < this.passwordTable.getRowCount() && null != this.selectedItem) {
            this.selectedItem.remove(selectedRow);
            this.updatePasswordTable();
        }
    }

    private void createUIComponents() {
        this.storageTree = new JTree(new DefaultMutableTreeNode("root"));
        this.storageTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        this.tableModel = new PasswordStorageDataModel();
        this.passwordTable = new JTable();
        this.passwordTable.setModel(this.tableModel);
        this.passwordTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        RightClickSelectTableMouseListener rightClickSelectTableMouseListener = new RightClickSelectTableMouseListener(this.passwordTable);
        this.passwordTable.addMouseListener(rightClickSelectTableMouseListener);

        PasswordTablePopupMenu passwordTablePopupMenu = new PasswordTablePopupMenu();
        passwordTablePopupMenu.addCopyUsernameActionListener(e -> onCopyUsername());
        passwordTablePopupMenu.addCopyPasswordActionListener(e -> onCopyPassword());
        passwordTablePopupMenu.addCopyUrlActionListener(e -> onCopyUrl());
        passwordTablePopupMenu.addCopyPortActionListener(e -> onCopyPort());
        passwordTablePopupMenu.addEditPasswordActionListener(e -> onEditPassword());
        passwordTablePopupMenu.addDeletePasswordActionListener(e -> onDeletePassword());
        this.passwordTable.setComponentPopupMenu(passwordTablePopupMenu);
    }
}
