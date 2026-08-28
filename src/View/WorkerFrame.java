/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author Ali khalifeh
 */
import Controller.Controller;
import Model.Client;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class WorkerFrame extends javax.swing.JFrame {

    /**
     * Creates new form WorkerFrame
     */
    Controller controller;
    // form fields — Add New Client
    private JTextField nameField, usernameField, emailField, phoneField;
    private JPasswordField passField;
    private JComboBox<String> accountTypeBox;

    // form fields — Find Client
    private JTextField searchField;
    private JTextArea resultArea;
    //Save client searched 
    private Client lastSearchedClient;
    private JButton deactivateBtn;
    private JButton activateBtn;

    public WorkerFrame() {
        initComponents();
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        controller = new Controller();

        setTitle("Bank Management System - Worker Console");
        setSize(950, 680);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(7, 21, 39)); // navy-deep

        buildToolbar();
        buildWelcome();
        buildAddClientPanel();
        buildFindClientPanel();

        setVisible(true);

    }

    // ---------- TOOLBAR ----------
    private void buildToolbar() {
        JPanel toolbar = new JPanel(null);
        toolbar.setBounds(0, 0, 950, 42);
        toolbar.setBackground(new Color(13, 36, 68));
        add(toolbar);

        JLabel title = new JLabel("BANK MANAGEMENT SYSTEM — WORKER CONSOLE");
        title.setBounds(16, 0, 500, 42);
        title.setForeground(new Color(201, 211, 224));
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toolbar.add(title);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(850, 6, 84, 30);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBackground(new Color(255, 80, 80, 40));
        logoutBtn.setForeground(new Color(255, 138, 138));
        logoutBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 80, 80, 120)));
        logoutBtn.addActionListener(e -> logout());
        toolbar.add(logoutBtn);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame.activeWorker = null;
            LoginFrame.activeWorkerId = 0;
            LoginFrame.activeWorkerName = null;
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    // ---------- WELCOME ----------
    private void buildWelcome() {
        String displayName = (LoginFrame.activeWorkerName != null)
                ? LoginFrame.activeWorkerName
                : "Worker";

        JLabel welcome = new JLabel("Welcome, " + displayName);
        welcome.setBounds(30, 60, 500, 28);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(welcome);

        JLabel sub = new JLabel("Register new clients or look up an existing client below.");
        sub.setBounds(30, 90, 600, 20);
        sub.setForeground(new Color(201, 211, 224));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(sub);
    }

    // ---------- ADD NEW CLIENT PANEL ----------
    private void buildAddClientPanel() {
        GlassCard panel = new GlassCard();
        panel.setBounds(30, 130, 430, 490);
        panel.setLayout(null);
        add(panel);

        JLabel header = new JLabel("● ADD NEW CLIENT");
        header.setForeground(new Color(30, 144, 255));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBounds(20, 16, 300, 20);
        panel.add(header);

        int y = 50;
        nameField = addLabeledField(panel, "Full Name", y, 390);
        y += 60;
        usernameField = addLabeledField(panel, "Username", y, 390);
        y += 60;

        JLabel passLabel = new JLabel("PASSWORD");
        passLabel.setBounds(20, y, 390, 15);
        passLabel.setForeground(new Color(201, 211, 224));
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(passLabel);
        passField = new JPasswordField();
        passField.setBounds(20, y + 18, 390, 34);
        styleInput(passField);
        panel.add(passField);
        y += 60;

        emailField = addLabeledField(panel, "Email", y, 390);
        y += 60;
        phoneField = addLabeledField(panel, "Phone Number", y, 390);
        y += 60;

        JLabel typeLabel = new JLabel("ACCOUNT TYPE");
        typeLabel.setBounds(20, y, 390, 15);
        typeLabel.setForeground(new Color(201, 211, 224));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(typeLabel);
        accountTypeBox = new JComboBox<>(new String[]{"savings", "checking"});
        accountTypeBox.setBounds(20, y + 18, 390, 34);
        panel.add(accountTypeBox);
        y += 65;

        JButton saveBtn = new JButton("Save & Create Account");
        saveBtn.setBounds(20, y, 390, 42);
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.addActionListener(this::saveClientActionPerformed);
        panel.add(saveBtn);
    }

    private void saveClientActionPerformed(ActionEvent evt) {
        String full_name = nameField.getText().strip();
        String username = usernameField.getText().strip();
        String password = new String(passField.getPassword()).strip();
        String email = emailField.getText().strip();
        String phone = phoneField.getText().strip();
        String accountType = (String) accountTypeBox.getSelectedItem();

        if (full_name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the client's full name.");
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a username.");
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.");
            return;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a phone number.");
            return;
        }
        if (LoginFrame.activeWorkerId == 0) {
            JOptionPane.showMessageDialog(this,
                    "No worker session found. Please log out and log back in.",
                    "Session Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controller.registerClient(full_name, username, password, email, phone,
                    new Date(), LoginFrame.activeWorkerId, accountType, new Date());
            JOptionPane.showMessageDialog(this, "Client registered and account created.");
            nameField.setText("");
            usernameField.setText("");
            passField.setText("");
            emailField.setText("");
            phoneField.setText("");
        } catch (Exception ex) {
            Logger.getLogger(WorkerFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ---------- FIND CLIENT PANEL ----------
    private void buildFindClientPanel() {
        GlassCard panel = new GlassCard();
        panel.setBounds(490, 130, 430, 320);
        panel.setLayout(null);
        add(panel);

        JLabel header = new JLabel("● FIND CLIENT");
        header.setForeground(new Color(46, 204, 113));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBounds(20, 16, 300, 20);
        panel.add(header);

        JLabel searchLabel = new JLabel("SEARCH BY PHONE NUMBER");
        searchLabel.setBounds(20, 50, 390, 15);
        searchLabel.setForeground(new Color(201, 211, 224));
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(20, 68, 390, 34);
        styleInput(searchField);
        panel.add(searchField);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(20, 112, 390, 40);
        searchBtn.setBackground(new Color(7, 21, 39));
        searchBtn.setForeground(new Color(46, 204, 113));
        searchBtn.setBorder(BorderFactory.createLineBorder(new Color(46, 204, 113)));
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(this::searchClientActionPerformed);
        panel.add(searchBtn);

        resultArea = new JTextArea();
        resultArea.setBounds(20, 162, 390, 140);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(new Color(46, 204, 113, 20));
        resultArea.setForeground(new Color(201, 211, 224));
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(resultArea);

        deactivateBtn = new JButton("Deactivate Client");
        deactivateBtn.setBackground(new Color(255, 92, 92));
        deactivateBtn.setBounds(20, 292, 390, 34);
        deactivateBtn.setForeground(Color.WHITE);
        deactivateBtn.setFocusPainted(false);
        deactivateBtn.setBorderPainted(false);
        deactivateBtn.setVisible(false);
        deactivateBtn.addActionListener(this::deactivateClientActionPerformed);
        panel.add(deactivateBtn);

        activateBtn = new JButton("Activate Client");
        activateBtn.setBackground(new Color(46, 204, 113)); // green — opposite of deactivate's red
        activateBtn.setBounds(20, 292, 390, 34); // same position as deactivateBtn — only one shows at a time
        activateBtn.setForeground(Color.WHITE);
        activateBtn.setFocusPainted(false);
        activateBtn.setBorderPainted(false);
        activateBtn.setVisible(false);
        activateBtn.addActionListener(this::activateClientActionPerformed);
        panel.add(activateBtn);

    }

    private void deactivateClientActionPerformed(ActionEvent evt) {
        if (lastSearchedClient == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deactivate " + lastSearchedClient.getFull_name() + "? They will no longer be able to log in.",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean success = controller.deactivateClientByPhone(lastSearchedClient.getPhone_number());
            if (success) {
                JOptionPane.showMessageDialog(this, "Client deactivated.");
                resultArea.setText(resultArea.getText() + "\nStatus: INACTIVE");
                deactivateBtn.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "No matching client found to deactivate.");
            }
        } catch (Exception ex) {
            Logger.getLogger(WorkerFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void activateClientActionPerformed(ActionEvent evt) {
        if (lastSearchedClient == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Activate " + lastSearchedClient.getFull_name() + "? They will be able to log in.",
                "Confirm Deactivation",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            boolean success = controller.activateClientByPhone(lastSearchedClient.getPhone_number());
            if (success) {
                JOptionPane.showMessageDialog(this, "Client activated.");
                resultArea.setText(resultArea.getText() + "\nStatus: ACTIVE");
                deactivateBtn.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "No matching client found to activate.");
            }
        } catch (Exception ex) {
            Logger.getLogger(WorkerFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void searchClientActionPerformed(ActionEvent evt) {
        String query = searchField.getText().strip();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a phone number to search.");
            return;
        }

        try {
            Client client = controller.getClientByPhone(query);
            if (client == null) {
                resultArea.setText("No client found for \"" + query + "\".");
                lastSearchedClient = null;
                return;
            }

            lastSearchedClient = client;
            if (client.getIs_active()) {         
                deactivateBtn.setVisible(true);
                activateBtn.setVisible(false);
            } else {
                deactivateBtn.setVisible(false);
                activateBtn.setVisible(true);
            }
            String[] account = controller.getAccountByClientId(client.getClient_id());
            String balanceText = (account != null) ? account[2] : "N/A";
            String accountIdText = (account != null) ? account[0] : "N/A";

            resultArea.setText(
                    "ID: " + client.getClient_id() + "\n"
                    + "Name: " + client.getFull_name() + "\n"
                    + "Phone: " + client.getPhone_number() + "\n"
                    + "Account #: " + accountIdText + "\n"
                    + "Balance: $" + balanceText
            );
        } catch (Exception ex) {
            Logger.getLogger(WorkerFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- HELPERS ----------
    private JTextField addLabeledField(JPanel panel, String labelText, int y, int width) {
        JLabel label = new JLabel(labelText.toUpperCase());
        label.setBounds(20, y, width, 15);
        label.setForeground(new Color(201, 211, 224));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(label);

        JTextField field = new JTextField();
        field.setBounds(20, y + 18, width, 34);
        styleInput(field);
        panel.add(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setBackground(new Color(255, 255, 255, 15));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40)));
    }

    /**
     * Custom rounded, semi-transparent "glass" panel used for the two cards,
     * matching the GlassPanel style already used in LoginFrame.
     */
    private static class GlassCard extends JPanel {

        public GlassCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 15));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WorkerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorkerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorkerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WorkerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WorkerFrame().setVisible(true);
            }
        });
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

