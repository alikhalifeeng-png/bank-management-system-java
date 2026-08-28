/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 *
 * @author Ali khalifeh
 */ 
public class Client {

   
    private int client_id ;
    private String full_name;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private Date date_registered;
    private boolean is_active;
    private int created_by;

    public Client(int client_id, String full_name, String username, String password, String email,
                  String phone_number, Date date_registered, int created_by,boolean is_active) {
        this.client_id = client_id;
        this.full_name = full_name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
        this.date_registered = date_registered;
        this.created_by = created_by;
        this.is_active = is_active;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getDate_registered() {
        return date_registered;
    }

    public void setDate_registered(Date date_registered) {
        this.date_registered = date_registered;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }
    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    } 
    
}
