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
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientPage extends javax.swing.JFrame {

    Controller controller;
    private String[] activeAccount; // {account_id, account_type, balance, date_opened}

    private JLabel balanceLabel;
    private JLabel accountInfoLabel;

    private PlaceholderTextField depositField;
    private PlaceholderTextField withdrawField;
    private PlaceholderTextField transferToField;
    private PlaceholderTextField transferAmountField;

    private DefaultTableModel historyModel;

    public ClientPage() {
        initComponents();
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        controller = new Controller();

        setTitle("Bank Management System - Client Portal");
        setSize(1000, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(7, 21, 39)); // navy-deep

        loadActiveAccount();

        buildToolbar();
        buildWelcome();
        buildBalanceCard();
        buildActionPanels();
        buildHistoryPanel();

        setVisible(true);
    }

    private void loadActiveAccount() {
        try {
            activeAccount = controller.getAccountByClientId(LoginFrame.activeClient.getClient_id());
        } catch (Exception ex) {
            Logger.getLogger(ClientPage.class.getName()).log(Level.SEVERE, null, ex);
            activeAccount = null;
        }
    }

    // ---------- TOOLBAR ----------
    private void buildToolbar() {
        JPanel toolbar = new JPanel(null);
        toolbar.setBounds(0, 0, 1000, 42);
        toolbar.setBackground(new Color(13, 36, 68));
        add(toolbar);

        JLabel title = new JLabel("BANK MANAGEMENT SYSTEM — CLIENT PORTAL");
        title.setBounds(16, 0, 500, 42);
        title.setForeground(new Color(201, 211, 224));
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        toolbar.add(title);

        JButton logoutBtn = new SolidButton("Logout", new Color(120, 30, 30), Color.WHITE);
        logoutBtn.setBounds(900, 6, 84, 30);
        logoutBtn.addActionListener(e -> logout());
        toolbar.add(logoutBtn);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LoginFrame.activeClient = null;
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }

    // ---------- WELCOME ----------
    private void buildWelcome() {
        String name = (LoginFrame.activeClient != null) ? LoginFrame.activeClient.getFull_name() : "Client";

        JLabel welcome = new JLabel("Welcome back, " + name);
        welcome.setBounds(30, 58, 500, 28);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(welcome);

        JLabel sub = new JLabel("View your balance, manage transactions, and check your history.");
        sub.setBounds(30, 88, 650, 20);
        sub.setForeground(new Color(201, 211, 224));
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        add(sub);
    }

    // ---------- BALANCE CARD ----------
    private void buildBalanceCard() {
        GradientCard card = new GradientCard(new Color(18, 58, 103), new Color(11, 37, 68));
        card.setBounds(30, 120, 940, 90);
        card.setLayout(null);
        add(card);

        JLabel label = new JLabel("CURRENT BALANCE");
        label.setBounds(24, 14, 300, 15);
        label.setForeground(new Color(201, 211, 224));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        card.add(label);

        String balanceText = (activeAccount != null) ? activeAccount[2] : "N/A";
        balanceLabel = new JLabel("$" + balanceText);
        balanceLabel.setBounds(24, 32, 400, 40);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        card.add(balanceLabel);

        String acctText = (activeAccount != null)
                ? "Account #" + activeAccount[0] + "\n" + capitalize(activeAccount[1]) + " Account"
                : "No account on file";
        accountInfoLabel = new JLabel("<html><div style='text-align:right;'>" + acctText.replace("\n", "<br>") + "</div></html>");
        accountInfoLabel.setBounds(700, 20, 220, 50);
        accountInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        accountInfoLabel.setForeground(new Color(201, 211, 224));
        accountInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        card.add(accountInfoLabel);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    // ---------- ACTION PANELS ----------
    private void buildActionPanels() {
        int panelW = 300, panelH = 170, y = 225, gap = 20;

        // Deposit
        GlassCard depositPanel = new GlassCard();
        depositPanel.setBounds(30, y, panelW, panelH);
        depositPanel.setLayout(null);
        add(depositPanel);

        JLabel depositHeader = new JLabel("● DEPOSIT");
        depositHeader.setForeground(new Color(46, 204, 113));
        depositHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        depositHeader.setBounds(18, 14, 200, 20);
        depositPanel.add(depositHeader);

        depositField = addAmountField(depositPanel, 50);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(18, 110, panelW - 36, 38);
        depositBtn.setBackground(new Color(46, 204, 113));
        depositBtn.setForeground(new Color(6, 51, 27));
        depositBtn.setFocusPainted(false);
        depositBtn.setBorderPainted(false);
        depositBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        depositBtn.addActionListener(this::depositActionPerformed);
        depositPanel.add(depositBtn);

        // Withdraw
        GlassCard withdrawPanel = new GlassCard();
        withdrawPanel.setBounds(30 + panelW + gap, y, panelW, panelH);
        withdrawPanel.setLayout(null);
        add(withdrawPanel);

        JLabel withdrawHeader = new JLabel("● WITHDRAW");
        withdrawHeader.setForeground(new Color(255, 92, 92));
        withdrawHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        withdrawHeader.setBounds(18, 14, 200, 20);
        withdrawPanel.add(withdrawHeader);

        withdrawField = addAmountField(withdrawPanel, 50);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(18, 110, panelW - 36, 38);
        withdrawBtn.setBackground(new Color(255, 92, 92));
        withdrawBtn.setForeground(new Color(58, 0, 0));
        withdrawBtn.setFocusPainted(false);
        withdrawBtn.setBorderPainted(false);
        withdrawBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        withdrawBtn.addActionListener(this::withdrawActionPerformed);
        withdrawPanel.add(withdrawBtn);

        // Transfer
        GlassCard transferPanel = new GlassCard();
        transferPanel.setBounds(30 + 2 * (panelW + gap), y, panelW, panelH);
        transferPanel.setLayout(null);
        add(transferPanel);

        JLabel transferHeader = new JLabel("● TRANSFER");
        transferHeader.setForeground(new Color(30, 144, 255));
        transferHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        transferHeader.setBounds(18, 14, 200, 20);
        transferPanel.add(transferHeader);

        JLabel toLabel = new JLabel("TO ACCOUNT #");
        toLabel.setBounds(18, 40, panelW - 36, 14);
        toLabel.setForeground(new Color(201, 211, 224));
        toLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        transferPanel.add(toLabel);

        transferToField = new PlaceholderTextField("e.g. 12");
        styleInput(transferToField);
        transferToField.setBounds(18, 56, panelW - 36, 28);
        transferPanel.add(transferToField);

        JLabel amtLabel = new JLabel("AMOUNT");
        amtLabel.setBounds(18, 88, panelW - 36, 14);
        amtLabel.setForeground(new Color(201, 211, 224));
        amtLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        transferPanel.add(amtLabel);

        transferAmountField = new PlaceholderTextField("0.00");
        styleInput(transferAmountField);
        transferAmountField.setBounds(18, 104, panelW - 36, 28);
        transferPanel.add(transferAmountField);

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBounds(18, 138, panelW - 36, 26);
        transferBtn.setBackground(new Color(30, 144, 255));
        transferBtn.setForeground(Color.WHITE);
        transferBtn.setFocusPainted(false);
        transferBtn.setBorderPainted(false);
        transferBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        transferBtn.addActionListener(this::transferActionPerformed);
        transferPanel.add(transferBtn);
    }

    private PlaceholderTextField addAmountField(JPanel panel, int y) {
        JLabel label = new JLabel("AMOUNT");
        label.setBounds(18, y, 260, 14);
        label.setForeground(new Color(201, 211, 224));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        panel.add(label);

        PlaceholderTextField field = new PlaceholderTextField("0.00");
        field.setBounds(18, y + 16, 264, 30);
        styleInput(field);
        panel.add(field);
        return field;
    }

    private void styleInput(JTextField field) {
        field.setOpaque(true);
        field.setBackground(new Color(20, 40, 70)); // solid dark navy — Nimbus ignores setOpaque(false) on text fields
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setSelectionColor(new Color(30, 144, 255));
        field.setSelectedTextColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40)));
    }

    // ---------- ACTION HANDLERS ----------
    private void depositActionPerformed(ActionEvent evt) {
        if (!hasAccount()) {
            return;
        }
        BigDecimal amount = parseAmount(depositField.getText());
        if (amount == null) {
            return;
        }

        try {
            controller.deposite(Integer.parseInt(activeAccount[0]), amount);
            JOptionPane.showMessageDialog(this, "Deposit successful.");
            depositField.setText("");
            refreshBalanceAndHistory();
        } catch (Exception ex) {
            Logger.getLogger(ClientPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Deposit Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdrawActionPerformed(ActionEvent evt) {
        if (!hasAccount()) {
            return;
        }
        BigDecimal amount = parseAmount(withdrawField.getText());
        if (amount == null) {
            return;
        }

        try {
            controller.withdraw(Integer.parseInt(activeAccount[0]), amount);
            JOptionPane.showMessageDialog(this, "Withdrawal successful.");
            withdrawField.setText("");
            refreshBalanceAndHistory();
        } catch (Exception ex) {
            Logger.getLogger(ClientPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Withdraw Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transferActionPerformed(ActionEvent evt) {
        if (!hasAccount()) {
            return;
        }

        String toText = transferToField.getText().strip();
        if (toText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the destination account number.");
            return;
        }
        int toAccountId;
        try {
            toAccountId = Integer.parseInt(toText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Destination account number must be numeric.");
            return;
        }

        BigDecimal amount = parseAmount(transferAmountField.getText());
        if (amount == null) {
            return;
        }

        try {
            controller.transfer(Integer.parseInt(activeAccount[0]), toAccountId, amount);
            JOptionPane.showMessageDialog(this, "Transfer successful.");
            transferToField.setText("");
            transferAmountField.setText("");
            refreshBalanceAndHistory();
        } catch (Exception ex) {
            Logger.getLogger(ClientPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transfer Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hasAccount() {
        if (activeAccount == null) {
            JOptionPane.showMessageDialog(this,
                    "No account found for this client. Contact a worker.",
                    "Account Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private BigDecimal parseAmount(String text) {
        text = text.strip();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an amount.");
            return null;
        }
        try {
            BigDecimal amount = new BigDecimal(text);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.");
                return null;
            }
            return amount; 
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric amount.");
            return null;
        }
    }

    private void refreshBalanceAndHistory() {
        loadActiveAccount();
        if (activeAccount != null) {
            balanceLabel.setText("$" + activeAccount[2]);
        }
        loadHistory();
    }

    // ---------- HISTORY PANEL ----------
    private void buildHistoryPanel() {
        GlassCard panel = new GlassCard();
        panel.setBounds(30, 410, 940, 250);
        panel.setLayout(null);
        add(panel);

        JLabel header = new JLabel("TRANSACTION HISTORY");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBounds(18, 14, 300, 20);
        panel.add(header);

        String[] columns = {"Date", "Type", "Amount", "Target Account"};
        historyModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(historyModel);
        table.setRowHeight(24);
        table.setOpaque(true);
        table.setBackground(new Color(11, 31, 58));
        table.setForeground(new Color(223, 230, 238));
        table.setGridColor(new Color(255, 255, 255, 20));
        table.setFillsViewportHeight(true);
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBackground(new Color(13, 36, 68));
        table.getTableHeader().setForeground(new Color(201, 211, 224));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 30)));

        // Force each cell renderer to use our colors too — Nimbus otherwise
        // paints its own per-cell background regardless of table-level settings.
        javax.swing.table.DefaultTableCellRenderer cellRenderer = new javax.swing.table.DefaultTableCellRenderer();
        cellRenderer.setOpaque(true);
        cellRenderer.setBackground(new Color(11, 31, 58));
        cellRenderer.setForeground(new Color(223, 230, 238));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(18, 44, 904, 190);
        scrollPane.getViewport().setBackground(new Color(11, 31, 58));
        panel.add(scrollPane);

        loadHistory();
    }

    private void loadHistory() {
        historyModel.setRowCount(0);
        if (activeAccount == null) {
            return;
        }

        try {
            ArrayList<String[]> history = controller.getTransactionHistory(Integer.parseInt(activeAccount[0]));
            for (String[] row : history) {
                // row = {transaction_id, type, amount, transaction_date, target_account}
                String target = (row[4] != null) ? row[4] : "—";
                historyModel.addRow(new Object[]{row[3], capitalize(row[1]), "$" + row[2], target});
            }
        } catch (Exception ex) {
            Logger.getLogger(ClientPage.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Could not load transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gradient-filled card used for the balance banner.
     */
    private static class GradientCard extends JPanel {

        private final Color colorA, colorB;

        public GradientCard(Color colorA, Color colorB) {
            this.colorA = colorA;
            this.colorB = colorB;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, colorA, getWidth(), getHeight(), colorB);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Rounded, semi-transparent "glass" panel — same style used across the app.
     */
    private static class SolidButton extends JButton {

        private final Color bg;
        private final Color fg;

        public SolidButton(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
            g2.dispose();
            super.paintComponent(g);
        }
    }

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
     * Text field with a custom-painted placeholder — getText() stays truly
     * empty until the user types, avoiding the placeholder-saved-as-data bug.
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
            java.util.logging.Logger.getLogger(ClientPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientPage().setVisible(true);
            }
        });
    }

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
