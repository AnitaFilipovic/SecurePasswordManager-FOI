package spm.gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PasswordTablePopupMenu extends JPopupMenu {
    private final JMenuItem itemCopyUsername;
    private final JMenuItem itemCopyPassword;
    private final JMenuItem itemCopyUrl;
    private final JMenuItem itemCopyPort;
    private final JMenuItem itemEditPassword;
    private final JMenuItem itemDeletePassword;

    public PasswordTablePopupMenu() {
        this.itemCopyUsername = new JMenuItem("Copy username");
        this.itemCopyPassword = new JMenuItem("Copy password");
        this.itemCopyUrl = new JMenuItem("Copy URL");
        this.itemCopyPort = new JMenuItem("Copy port");
        this.itemEditPassword = new JMenuItem("Edit password");
        this.itemDeletePassword = new JMenuItem("Delete password");

        this.add(itemCopyUsername);
        this.add(itemCopyPassword);
        this.add(itemCopyUrl);
        this.add(itemCopyPort);
        this.addSeparator();
        this.add(itemEditPassword);
        this.add(itemDeletePassword);
    }

    public void addCopyUsernameActionListener(ActionListener l) {
        this.itemCopyUsername.addActionListener(l);
    }

    public void addCopyPasswordActionListener(ActionListener l) {
        this.itemCopyPassword.addActionListener(l);
    }

    public void addCopyUrlActionListener(ActionListener l) {
        this.itemCopyUrl.addActionListener(l);
    }

    public void addCopyPortActionListener(ActionListener l) {
        this.itemCopyPort.addActionListener(l);
    }

    public void addEditPasswordActionListener(ActionListener l) {
        this.itemEditPassword.addActionListener(l);
    }

    public void addDeletePasswordActionListener(ActionListener l) {
        this.itemDeletePassword.addActionListener(l);
    }
}
