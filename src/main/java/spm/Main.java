package spm;

import spm.gui.MainForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            System.err.println("Failed to initialize FlatLaf. Reason: " + e.getMessage());
        }
        MainForm.run();
    }
}