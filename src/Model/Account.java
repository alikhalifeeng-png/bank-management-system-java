/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Ali khalifeh
 */
public class Account {
    private int account_id;
    private int client_id;
    private String account_type;
    private BigDecimal balance;
    private Date date_opened;

    public Account() {}

    public Account(int account_id, int client_id, String account_type, BigDecimal balance, Date date_opened) {
        this.account_id = account_id;
        this.client_id = client_id;
        this.account_type = account_type;
        this.balance = balance;
        this.date_opened = date_opened;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDate_opened() {
        return date_opened;
    }

    public void setDate_opened(Date date_opened) {
        this.date_opened = date_opened;
    }
    
    
    
}
