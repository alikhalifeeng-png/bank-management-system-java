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
public class Transaction {
    private int transaction_id;
    private int account_id;
    private String type;
    private BigDecimal amount;
    private Date transaction_date;
    private Integer target_account;

    public Transaction(int transaction_id, int account_number, String type, BigDecimal amount, Date transaction_date, int target_account) {
        this.transaction_id = transaction_id;
        this.account_id = account_number;
        this.type = type;
        this.amount = amount;
        this.transaction_date = transaction_date;
        this.target_account = target_account;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getAccount_number() {
        return account_id;
    }

    public void setAccount_number(int account_number) {
        this.account_id = account_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public int getTarget_account() {
        return target_account;
    }

    public void setTarget_account(int target_account) {
        this.target_account = target_account;
    }
    
    
    
}
