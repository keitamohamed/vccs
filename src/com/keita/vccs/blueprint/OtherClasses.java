package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;

public class OtherClasses {
    private SimpleStringProperty emp;
    private SimpleStringProperty classID;
    private SimpleStringProperty className;

    public OtherClasses(String emp, String classID, String className) {
        this.emp = new SimpleStringProperty(emp);
        this.classID = new SimpleStringProperty(classID);
        this.className = new SimpleStringProperty(className);
    }

    public String getEmp() {
        return emp.get();
    }

    public String getClassID() {
        return classID.get();
    }

    public String getClassName() {
        return className.get();
    }
}
