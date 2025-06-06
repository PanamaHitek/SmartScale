package com.panama_hitek;

public class SmartScale {

    public static void main(String[] args) {

        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore and use default look and feel
        }
        EnvironmentChecker.checkEnvironment();
        new JFrameWindow().setVisible(true);
    }
}
