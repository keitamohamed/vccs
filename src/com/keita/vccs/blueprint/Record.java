package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;

public class Record {
    private SimpleStringProperty emp;
    private SimpleStringProperty classID;
    private SimpleStringProperty name;
    private SimpleStringProperty className;
    private SimpleStringProperty unite;
    private SimpleStringProperty grade;
    private SimpleStringProperty term;
    private SimpleStringProperty year;

    public Record(String classID, String cName, String unite, String grade,
                  String term, String year){
        this.classID = new SimpleStringProperty(classID);
        this.className = new SimpleStringProperty(cName);
        this.unite = new SimpleStringProperty(unite);
        this.grade = new SimpleStringProperty(grade);
        this.term = new SimpleStringProperty(term);
        this.year = new SimpleStringProperty(year);

    }

    public Record(String emp, String classID, String name, String className,
                  String unite, String grade,  String term, String year) {
        this.emp = new SimpleStringProperty(emp);
        this.classID = new SimpleStringProperty(classID);
        this.name = new SimpleStringProperty(name);
        this.className = new SimpleStringProperty(className);
        this.unite = new SimpleStringProperty(unite);
        this.grade = new SimpleStringProperty(grade);
        this.term = new SimpleStringProperty(term);
        this.year = new SimpleStringProperty(year);
    }

    public Record (String emp, String classID, String name, String className) {
        this.emp = new SimpleStringProperty(emp);
        this.classID = new SimpleStringProperty(classID);
        this.name = new SimpleStringProperty(name);
        this.className = new SimpleStringProperty(className);
    }

    public String getEmp() {
        return emp.get();
    }

    public String getClassID() {
        return classID.get();
    }

    public String getName() {
        return name.get();
    }

    public String getClassName() {
        return className.get();
    }

    public String getUnite() {
        return unite.get();
    }

    public String getGrade() {
        return grade.get();
    }

    public String getTerm() {
        return term.get();
    }

    public String getYear() {
        return year.get();
    }
}
