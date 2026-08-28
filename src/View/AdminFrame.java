/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.Controller;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Ali khalifeh
 */
public class AdminFrame extends javax.swing.JFrame {

    /**
     * Creates new form AdminFrame
     */
    Controller controller;
    private PlaceholderTextField workerName;
    private PlaceholderTextField workerUsername;
    private PlaceholderTextField workerPass;

    public AdminFrame() {
        initComponents();
        controller = new Controller();
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        controller = new Controller();

        setTitle("Bank Management System - Advisor Console");
        setSize(950, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(7, 21, 39)); // navy-deep

        buildToolbar();
        buildWelcome();
        buildActionCards();
        buildAddWorkerPanel();

        setVisible(true);

    }

    // ---------- TOOLBAR ----------
    private void buildToolbar() {
        JPanel toolbar = new JPanel(null);
        toolbar.setBounds(0, 0, 950, 42);
        toolbar.setBackground(new Color(13, 36, 68));
        add(toolbar);

        JLabel title = new JLabel("BANK MANAGEMENT SYSTEM — ADVISOR CONSOLE");
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
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    // ---------- WELCOME ----------
    private void buildWelcome() {
        JLabel welcome = new JLabel("Welcome, Advisor");
        welcome.setBounds(30, 60, 500, 28);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(welcome);

        JLabel sub = new JLabel("Manage workers and monitor all client accounts from here.");
        sub.setBounds(30, 90, 600, 20);
        sub.setForeground(new Color(201, 211, 224));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(sub);
    }

    // ---------- ACTION CARDS ----------
    private void buildActionCards() {
        String[][] cards = {
            {"+ Add a Worker", "Create a new worker login"},
            {"View Clients", "All clients & balances"},
            {"View Workers", "All active workers"}
        };
        Color[] cardColors = {
            new Color(95, 191, 46), // green
            new Color(209, 174, 31), // amber
            new Color(155, 48, 201) // purple
        };

        int startX = 30, y = 130, w = 280, h = 90, gap = 15;

        for (int i = 0; i < 3; i++) {
            int x = startX + i * (w + gap);
            GradientCard card = new GradientCard(cardColors[i]);
            card.setBounds(x, y, w, h);
            card.setLayout(null);
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add(card);

            JLabel big = new JLabel(cards[i][0]);
            big.setBounds(16, 16, w - 32, 22);
            big.setFont(new Font("Segoe UI", Font.BOLD, 15));
            big.setForeground(new Color(11, 31, 58));
            card.add(big);

            JLabel small = new JLabel(cards[i][1]);
            small.setBounds(16, 42, w - 32, 18);
            small.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            small.setForeground(new Color(11, 31, 58));
            card.add(small);

            final int index = i;
            MouseAdapter clickHandler = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onCardClicked(index);
                }
            };
            card.addMouseListener(clickHandler);
            big.addMouseListener(clickHandler);
            small.addMouseListener(clickHandler);
        }
    }

    private void onCardClicked(int index) {
        switch (index) {
            case 0:
                // "Add a Worker" — scrolls focus to the form panel already on screen
                workerName.requestFocus();
                break;
            case 1:
                new ViewClients();
                break;
            case 2:
                new ViewWorkers();
                break;
        }
    }

    // ---------- ADD WORKER PANEL ----------
    private void buildAddWorkerPanel() {
        GlassCard panel = new GlassCard();
        panel.setBounds(30, 250, 610, 220);
        panel.setLayout(null);
        add(panel);

        JLabel header = new JLabel("● ADD A WORKER");
        header.setForeground(new Color(95, 191, 46));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBounds(20, 16, 300, 20);
        panel.add(header);

        int fieldY = 55, fieldW = 180, gap = 15;

        workerName = new PlaceholderTextField("e.g. Sara Fares");
        addLabeledField(panel, "Full Name", workerName, 20, fieldY, fieldW);

        workerUsername = new PlaceholderTextField("username");
        addLabeledField(panel, "Username", workerUsername, 20 + fieldW + gap, fieldY, fieldW);

        workerPass = new PlaceholderTextField("min 6 chars");
        addLabeledField(panel, "Password", workerPass, 20 + 2 * (fieldW + gap), fieldY, fieldW);

        JButton saveBtn = new JButton("Save Worker");
        saveBtn.setBounds(20, fieldY + 75, 180, 40);
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.addActionListener(this::saveWorkerActionPerformed);
        panel.add(saveBtn);
    }

    private void addLabeledField(JPanel panel, String labelText, PlaceholderTextField field, int x, int y, int width) {
        JLabel label = new JLabel(labelText.toUpperCase());
        label.setBounds(x, y, width, 15);
        label.setForeground(new Color(201, 211, 224));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(label);

        field.setBounds(x, y + 18, width, 34);
        field.setBackground(new Color(255, 255, 255, 15));
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40)));
        panel.add(field);
    }

    private void saveWorkerActionPerformed(ActionEvent evt) {
        String full_name = workerName.getText().strip();
        String username = workerUsername.getText().strip();
        String password = workerPass.getText().strip();

        if (full_name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter worker's full name to proceed.");
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a username to proceed.");
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter worker's password to proceed.");
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters.");
            return;
        }

        try {
            controller.addWorker(full_name, username, password);
            JOptionPane.showMessageDialog(this, "Worker added successfully.");
            workerName.setText("");
            workerUsername.setText("");
            workerPass.setText("");
        } catch (Exception ex) {
            Logger.getLogger(AdminFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Gradient-filled card used for the three top action buttons.
     */
    private static class GradientCard extends JPanel {

        private final Color baseColor;

        public GradientCard(Color baseColor) {
            this.baseColor = baseColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color lighter = baseColor.brighter();
            GradientPaint gradient = new GradientPaint(0, 0, lighter, getWidth(), getHeight(), baseColor);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Rounded, semi-transparent "glass" panel — same style used in
     * WorkerFrame/LoginFrame.
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
     * Text field with a custom-painted placeholder. getText() stays truly empty
     * until the user types — no risk of placeholder text being saved as real
     * data.
     */
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
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminFrame().setVisible(true);
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

        workerAdd2 = new javax.swing.JButton();

        workerAdd2.setBackground(new java.awt.Color(255, 255, 102));
        workerAdd2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        workerAdd2.setText("View Clients ");
        workerAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workerAdd2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 808, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }


    private void workerAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workerAdd2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_workerAdd2ActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton workerAdd2;
    // End of variables declaration//GEN-END:variables

}
