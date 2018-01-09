package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Class {

    private SimpleStringProperty  classID;
    private SimpleStringProperty className;
    private SimpleStringProperty description;
    private ObservableList<Student> student;

    public Class() {};

    public Class(String classID, String className, String description) {
        this.classID = new SimpleStringProperty(classID);
        this.className = new SimpleStringProperty(className);
        student = FXCollections.observableArrayList();
        this.description = new SimpleStringProperty(description);
    }

    public Student isSPresent(String search) {
        Student student = findStudent(search);
        if (student == null) {
            System.out.println("This name or id# " + search + " is not in the student list.");
            return null;
        }
        System.out.println(student.getName() + " with an emp# " + student.getId() + " is in the list.\n");
        return student;
    }

    private Student findStudent (String search) {
        if (studentSize() > 0) {
            for (Student student : student) {
                if (student.getName().equalsIgnoreCase(search) ||
                        student.getId().equalsIgnoreCase(search)) {
                    return student;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Class ID " + classID + " name " + className +
                " description " + description;
    }

    private int studentSize() {
        return student.size();
    }

    public String getClassID() {
        return classID.get();
    }

    public String getClassName() {
        return className.get();
    }

    public String getDescription() {
        return description.get();
    }

    public ObservableList<Student> getStudent() {
        return student;
    }
}
