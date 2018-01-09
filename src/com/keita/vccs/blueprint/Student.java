package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Student {
    private SimpleStringProperty id;
    private SimpleStringProperty classID;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private ObservableList<Grade> grades;

    public Student () {
        grades = FXCollections.observableArrayList();
    }

    public Student(String id, String classID, String name, String email, String phone) {
        this.id = new SimpleStringProperty(id);
        this.classID = new SimpleStringProperty(classID);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        grades = FXCollections.observableArrayList();
    }

    public Student(String id, String name, String email, String phone) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        grades = FXCollections.observableArrayList();
    }

    @Override
    public String toString() {
        return "EMP# " + id + ", name: " + name + ", email: " + email +
                " phone#: " + phone;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getClassID() {
        return classID.get();
    }

    public void setClassID(String classID) {
        this.classID.set(classID);
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

    public ObservableList<Grade> getGrades() {
        return grades;
    }
}
