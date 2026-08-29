/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.Controller;
import Model.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Ali khalifeh
 */
public class LoginFrame extends javax.swing.JFrame {

    /**
     * Creates new form LoginFrame
     */
    protected static String activeWorker;      // worker's username
    protected static int activeWorkerId;        // worker's DB id — used as created_by
    protected static String activeWorkerName;   // worker's full name — used for display
    protected static Client activeClient;
    Controller controller;
    // Hardcoded advisor credentials — no advisor table by design
    private static final String ADVISOR_USERNAME = "advisor";
    private static final String ADVISOR_PASSWORD = "admin123";

    public LoginFrame() {
        initComponents();
        controller = new Controller();

        setTitle("Bank Management System - Login");
        setSize(600, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel bgLabel = new JLabel();
        bgLabel.setBounds(0, 0, 600, 600);
        bgLabel.setLayout(null);
        try {
            Image img = ImageIO.read(getClass().getResource("/View/bank_background_v3.png"));
            Image scaled = img.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
            bgLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("Background Image Error: Check path /View/bank_background_v3.png");
        }
        this.setContentPane(bgLabel);

        GlassPanel glass = new GlassPanel();
        glass.setBounds(100, 120, 400, 340);
        glass.setLayout(null);
        bgLabel.add(glass);

        JLabel titleLabel = new JLabel("SYSTEM LOGIN", SwingConstants.CENTER);
        titleLabel.setBounds(0, 20, 400, 30);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        glass.add(titleLabel);

        PlaceholderTextField userField = new PlaceholderTextField("Username");
        userField.setBounds(50, 70, 300, 40);
        userField.setBackground(new Color(255, 255, 255, 40));
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100)));
        glass.add(userField);

        PlaceholderPasswordField passField = new PlaceholderPasswordField("Password");
        passField.setBounds(50, 130, 300, 40);
        passField.setBackground(new Color(255, 255, 255, 40));
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100)));
        glass.add(passField);

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(50, 200, 300, 45);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setBackground(new Color(30, 144, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        glass.add(loginBtn);

        /*JButton newClientBtn = new JButton("New Client? Register");
        newClientBtn.setBounds(50, 255, 300, 35);
        newClientBtn.setFocusPainted(false);
        newClientBtn.setBorderPainted(false);
        newClientBtn.setBackground(new Color(255, 255, 255, 60));
        newClientBtn.setForeground(Color.WHITE);
        newClientBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        glass.add(newClientBtn);*/
        JLabel timeLabel = new JLabel("", SwingConstants.RIGHT);
        JLabel dateLabel = new JLabel("", SwingConstants.RIGHT);

        timeLabel.setBounds(380, 500, 200, 40);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));

        dateLabel.setBounds(380, 535, 200, 20);
        dateLabel.setForeground(new Color(220, 220, 220));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        bgLabel.add(timeLabel);
        bgLabel.add(dateLabel);

        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
            dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        });
        timer.start();

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Please enter both username and password.",
                            "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                performLogin(username, password);
            }
        });

        /*newClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WorkerFrame().setVisible(true);
                LoginFrame.this.dispose();
            }
        });*/
        this.revalidate();
        this.repaint();

        this.revalidate();
        this.repaint();

        // Prevent Swing's default auto-focus on the first text field,
        // so its placeholder text renders correctly on initial load.
        SwingUtilities.invokeLater(() -> {
            this.requestFocusInWindow();
        });

    }

    private void performLogin(String username, String password) {
        // 1. Check Advisor first (hardcoded, no DB table)
        if (username.equals(ADVISOR_USERNAME) && password.equals(ADVISOR_PASSWORD)) {
            JOptionPane.showMessageDialog(this, "Welcome, Advisor!");
            new AdminFrame().setVisible(true);
            this.dispose();
            return;
        }

        // 2. Check Worker
        try {
            Worker worker = controller.loginWorker(username, password);
            if (worker != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + worker.getFull_name() + "!");
                activeWorker = worker.getUsername();
                activeWorkerId = worker.getWorker_id();
                activeWorkerName = worker.getFull_name();
                new WorkerFrame().setVisible(true);
                this.dispose();
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Check Client
        try {
            Client client = controller.loginClient(username, password);
            if (client != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + client.getFull_name() + "!");
                activeClient = client;
                new ClientPage().setVisible(true);
                this.dispose();
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Nothing matched — could be wrong credentials OR a deactivated client
        JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class PlaceholderTextField extends JTextField {

        private final String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 160, 175));
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                g2.drawString(placeholder, getInsets().left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {

        private final String placeholder;

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0 && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 160, 175));
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                g2.drawString(placeholder, getInsets().left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
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
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

