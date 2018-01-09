package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Teacher {

    private SimpleStringProperty empId;
    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleStringProperty phone;
    private ObservableList<Class> classes;

    public Teacher () {
        classes = FXCollections.observableArrayList();
    };
    public Teacher(String id, String name, String email, String phone) {
        this.empId = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleStringProperty(phone);
        classes = FXCollections.observableArrayList();
    }

    public boolean isAdded (String id, String name, String dsc) {
        Class cla = findClass(id, name);
            if (cla != null) {
                System.out.println("Class already existed.");
                return false;
            }
            addNewClass(id, name, dsc);
            return true;
    }

    private Class findClass (String id, String name) {
        if (classSize() > 0) {
            for (Class cla : classes) {
                if (cla.getClassID().equalsIgnoreCase(id) ||
                        cla.getClassName().equalsIgnoreCase(name)) {
                    return cla;
                }
            }
        }
        return null;
    }

    private void addNewClass (String id, String name, String desc) {
        classes.add(new Class(id, name, desc));

    }

    @Override
    public String toString() {
        return "EMP#: " + empId + ", name: " + name + " email: " + email +
                ", phone: " + phone + ", classes: " + classes;
    }

    public String getId() {
        return empId.get();
    }

    public void setId(String id) {
        this.empId.set(id);
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

    private int classSize() {
        return classes.size();
    }

    public ObservableList<Class> getClasses() {
        return classes;
    }
}
