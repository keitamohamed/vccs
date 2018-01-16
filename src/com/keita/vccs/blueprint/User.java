package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty userID;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private SimpleStringProperty userType;


    public User () {}

    public User (String id, String n, String e, String p, String type) {
        this.userID = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(n);
        this.email = new SimpleStringProperty(e);
        this.phone = new SimpleStringProperty(p);
        this.userType = new SimpleStringProperty(type);
    }

    @Override
    public String toString() {
        return "User ID: " + userID + " name " + name + " email " + email + " phone " + phone;
    }

    public String getUserID() {
        return userID.get();
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getUserType() {
        return userType.get();
    }

    public void setUserType(String userType) {
        this.userType.set(userType);
    }
}
