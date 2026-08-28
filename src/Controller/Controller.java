/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author Ali khalifeh
 */
public class Controller {

    Connection connection;
    Statement statement;

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db_java", "root", "");
        statement = connection.createStatement();
    }

    private void disconnect() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    public void addWorker(String full_name, String username, String password) throws Exception {
        String query = "INSERT INTO worker (full_name, username, password) VALUES ('"
                + full_name + "', '" + username + "', '" + password + "')";
        try {
            connect();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Worker not added.");
        } finally {
            disconnect();
        }
    }

    public void addAccount(int client_id, String account_type, BigDecimal balance, Date date_opened) throws Exception {
        String query = "INSERT INTO account (client_id, account_type, balance, date_opened) VALUES ("
                + client_id + ", '" + account_type + "', " + balance + ", '" + new java.sql.Date(date_opened.getTime()) + "')";
        try {
            connect();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Account not added.");
        } finally {
            disconnect();
        }
    }

    public void registerClient(String full_name, String username, String password, String email,
            String phone_number, Date date_registered, int created_by,
            String account_type, Date date_opened) throws Exception {
        String insertClient = "INSERT INTO client (full_name, username, password, email, phone_number, date_registered, created_by) VALUES ('"
                + full_name + "', '" + username + "', '" + password + "', '" + email + "', '"
                + phone_number + "', '" + new java.sql.Date(date_registered.getTime()) + "', " + created_by + ")";

        try {
            connect();
            connection.setAutoCommit(false);

            statement.executeUpdate(insertClient, Statement.RETURN_GENERATED_KEYS);
            ResultSet keys = statement.getGeneratedKeys();
            int newClientId = -1;
            if (keys.next()) {
                newClientId = keys.getInt(1);
            }

            String insertAccount = "INSERT INTO account (client_id, account_type, balance, date_opened) VALUES ("
                    + newClientId + ", '" + account_type + "', 0.00, '" + new java.sql.Date(date_opened.getTime()) + "')";
            statement.executeUpdate(insertAccount);

            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Client not saved — registration rolled back.");
        } finally {
            connection.setAutoCommit(true);
            disconnect();
        }
    }

    public void addTransaction(int account_id, String type, BigDecimal amount, Date transaction_date, Integer target_account) throws Exception {
        java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(transaction_date.getTime());
        String targetValue = (target_account == null) ? "NULL" : String.valueOf(target_account);

        String query = "INSERT INTO `transaction` (account_id, type, amount, transaction_date, target_account) VALUES ("
                + account_id + ", '" + type + "', " + amount + ", '" + sqlTimestamp + "', " + targetValue + ")";

        try {
            connect();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Transaction didn't proceed.");
        } finally {
            disconnect();
        }
    }

    

    public boolean deactivateClientByPhone(String phone_number) throws Exception {
        String query = "UPDATE client SET is_active = FALSE WHERE phone_number = '" + phone_number + "'";
        int numUpdated = 0;
        try {
            connect();
            numUpdated = statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Client deactivation failed.");
        } finally {
            disconnect();
        }
        return numUpdated > 0;
    }
    
        public boolean activateClientByPhone(String phone_number) throws Exception {
        String query = "UPDATE client SET is_active = TRUE WHERE phone_number = '" + phone_number + "'";
        int numUpdated = 0;
        try {
            connect();
            numUpdated = statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Client activation failed.");
        } finally {
            disconnect();
        }
        return numUpdated > 0;
    }

    

    public boolean deactivateWorkerById(String worker_id) throws Exception {
        String query = "UPDATE worker SET is_active = FALSE WHERE worker_id =" + worker_id;
        int numUpdated = 0;
        try {
            connect();
            numUpdated = statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("worker deactivation failed.");
        } finally {
            disconnect();
        }
        return numUpdated > 0;
    }

    public String[] getAccountByClientId(int client_id) throws Exception {
        String accountInfo[] = null;
        String query = "Select * from account where client_id = " + client_id;
        try {
            connect();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String account_id = resultSet.getString("account_id");
                String account_type = resultSet.getString("account_type");
                String balance = resultSet.getString("balance");
                Date date_opened = resultSet.getDate("date_opened");

                accountInfo = new String[]{account_id, account_type, balance, date_opened + ""};
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Account lookup failed.");
        } finally {
            disconnect();
        }

        return accountInfo;
    }

    public void deposite(int account_id, BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Deposit amount must be positive.");
        }
        try {
            connect();
            connection.setAutoCommit(false);

            statement.executeUpdate("UPDATE account SET balance = balance + " + amount + " WHERE account_id = " + account_id);

            String insertTransaction = "INSERT INTO `transaction` (account_id, type, amount, transaction_date, target_account) VALUES ("
                    + account_id + ", 'deposit', " + amount + ", '" + new java.sql.Timestamp(new Date().getTime()) + "', NULL)";
            statement.executeUpdate(insertTransaction);

            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Deposit failed.");
        } finally {
            connection.setAutoCommit(true);
            disconnect();
        }
    }

    public void withdraw(int account_id, BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Withdraw amount must be positive.");
        }
        try {
            connect();
            connection.setAutoCommit(false);

            ResultSet rs = statement.executeQuery("SELECT balance FROM account WHERE account_id = " + account_id);
            if (!rs.next()) {
                throw new Exception("Account not found.");
            }
            BigDecimal currentBalance = rs.getBigDecimal("balance");
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient funds.");
            }

            statement.executeUpdate("UPDATE account SET balance = balance - " + amount + " WHERE account_id = " + account_id);

            String insertTransaction = "INSERT INTO `transaction` (account_id, type, amount, transaction_date, target_account) VALUES ("
                    + account_id + ", 'withdraw', " + amount + ", '" + new java.sql.Timestamp(new Date().getTime()) + "', NULL)";
            statement.executeUpdate(insertTransaction);

            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Withdraw failed.");
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            throw ex; // re-throw "Insufficient funds" / "Account not found" as-is
        } finally {
            connection.setAutoCommit(true);
            disconnect();
        }
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Transfer amount must be positive.");
        }
        if (fromAccountId == toAccountId) {
            throw new Exception("Cannot transfer to the same account.");
        }
        try {
            connect();
            connection.setAutoCommit(false);

            ResultSet rs = statement.executeQuery("SELECT balance FROM account WHERE account_id = " + fromAccountId);
            if (!rs.next()) {
                throw new Exception("Source account not found.");
            }
            BigDecimal currentBalance = rs.getBigDecimal("balance");
            if (currentBalance.compareTo(amount) < 0) {
                throw new Exception("Insufficient funds for transfer.");
            }

            statement.executeUpdate("UPDATE account SET balance = balance - "
                    + amount + " WHERE account_id = " + fromAccountId);
            statement.executeUpdate("UPDATE account SET balance = balance + "
                    + amount + " WHERE account_id = " + toAccountId);

            String insertTransaction = "INSERT INTO `transaction` (account_id, type, amount, transaction_date, target_account) VALUES ("
                    + fromAccountId + ", 'transfer', " + amount + ", '"
                    + new java.sql.Timestamp(new Date().getTime()) + "', " + toAccountId + ")";
            statement.executeUpdate(insertTransaction);

            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Transfer failed.");
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException ex2) {
                /* ignore */ }
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            disconnect();
        }
    }

    public Worker loginWorker(String username, String password) throws Exception {
        Worker worker = null;
        String query = "Select * from worker where username = '" + username + "' And password = '" + password + "'";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                worker = new Worker(
                        rs.getInt("worker_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Login failed.");
        } finally {
            disconnect();
        }
        return worker; // null if username/password didn't match

    }

    public Client loginClient(String username, String password) throws Exception {
        Client client = null;
        String query = "SELECT * FROM client WHERE username = '" + username + "' AND password = '" + password + "' AND is_active = TRUE";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("date_registered"),
                        rs.getInt("created_by"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Login failed.");
        } finally {
            disconnect();
        }
        return client;

    }

    public ArrayList<String[]> getTransactionHistory(int account_id) throws Exception {
    ArrayList<String[]> history = new ArrayList<>();
    String query = "SELECT * FROM `transaction` WHERE account_id = " + account_id + " ORDER BY transaction_date DESC";
    try {
        connect();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            String transaction_id = rs.getString("transaction_id");
            String type = rs.getString("type");
            String amount = rs.getString("amount");
            String transaction_date = rs.getString("transaction_date");
            String target_account = rs.getString("target_account"); // may be null
            history.add(new String[]{transaction_id, type, amount, transaction_date, target_account});
        }
    } catch (SQLException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        throw new Exception("Could not load transaction history.");
    } finally {
        disconnect();
    }
    return history;
}

    public ArrayList<String[]> getAllClients() throws Exception {
        ArrayList<String[]> clients = new ArrayList<>();
        String query = "SELECT c.client_id, c.full_name, c.username, c.phone_number, c.is_active, a.balance, c.created_by"
                + " FROM client c LEFT JOIN account a ON c.client_id = a.client_id";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                clients.add(new String[]{
                    rs.getString("client_id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("phone_number"),
                    rs.getString("is_active"),
                    rs.getString("balance"),
                    rs.getString("created_by")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Could not load client list.");
        } finally {
            disconnect();
        }
        return clients;
    }

    public ArrayList<String[]> getAllActiveClients() throws Exception {
        ArrayList<String[]> clients = new ArrayList<>();
        String query = "SELECT c.client_id, c.full_name, c.username, c.phone_number, a.balance "
                + "FROM client c LEFT JOIN account a ON c.client_id = a.client_id "
                + "WHERE c.is_active = TRUE";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                clients.add(new String[]{
                    rs.getString("client_id"),
                    rs.getString("full_name"),
                    rs.getString("username"),
                    rs.getString("phone_number"),
                    rs.getString("balance")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Could not load active client list.");
        } finally {
            disconnect();
        }
        return clients;
    }

    public Client getClientByPhone(String phone) throws Exception {
        Client client = null;
        String query = "SELECT * FROM client WHERE phone_number = '" + phone + "'";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                client = new Client(
                        rs.getInt("client_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("date_registered"),
                        rs.getInt("created_by"),
                        rs.getBoolean("is_active")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Client lookup failed.");
        } finally {
            disconnect();
        }
        return client;
    }

    public boolean updateClientInfo(int client_id, String email, String phone_number) throws Exception {
        String query = "UPDATE client SET email = '" + email + "', phone_number = '" + phone_number + "' WHERE client_id = " + client_id;
        int numUpdated = 0;
        try {
            connect();
            numUpdated = statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Client update failed.");
        } finally {
            disconnect();
        }
        return numUpdated > 0;
    }

    public ArrayList<String[]> getAllWorkers() throws Exception {
        ArrayList<String[]> workers = new ArrayList<>();
        String query = "SELECT worker_id, full_name, username FROM worker";
        try {
            connect();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                workers.add(new String[]{
                    rs.getString("worker_id"),
                    rs.getString("full_name"),
                    rs.getString("username")
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Could not load worker list.");
        } finally {
            disconnect();
        }
        return workers;
    }

}
