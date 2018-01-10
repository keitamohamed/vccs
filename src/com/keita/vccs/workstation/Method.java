package com.keita.vccs.workstation;

import com.keita.vccs.associate.Export;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.message.Message;
import com.keita.vccs.sqlstatement.SQLStatement;
import com.keita.vccs.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class Method extends Calculation {

    private Message msg = new Message();
    private Validation validation = new Validation();
    private static Message ms = new Message();
    private Export export = new Export();

    private ObservableList<Teacher> teachers;
    private ObservableList<Class> classes;
    private ObservableList<Student> students;
    private static ObservableList<ScoreTable> scoreTables = FXCollections.observableArrayList();
    private static ObservableList<Record> rec = FXCollections.observableArrayList();

    private SQLStatement statement = new SQLStatement();
    private static TreeItem<ScoreTable> scoreItem = new TreeItem<>(new ScoreTable("EMP ID", "CLASS ID", "NAME",
            "SCORE NAME", "SCORE"));

    public Method() {
        teachers = FXCollections.observableArrayList();
        classes = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
    }

    public void addRecord(String emp, String classId, String sName,
                                 String cName, String unite, String grade, String term, String date) {
        statement.addRecord(emp, classId, sName, cName, unite, grade, term, date);
    }

    public void registerForClass(String emp, String techEMP, String classID, TextField aNameT) {
        if (!aNameT.isVisible()) {
            statement.registerForClass(emp, techEMP, classID);
        }else {
            // Assignment code go here

        }
    }

    public void exportStudentGrade() {
        export.exportStudentGrade(scoreTables);
        msg.alert("Successfully Exported", "Date was exported successfully as an excel file");
    }

    private void addRecord(String emp, String classID, String name, String classNme) {
        if (rec.size() == 0) {
            rec.add(new Record(emp, classID, name, classNme));
        }
        if (rec.size() > 0) {
            boolean exist = isExist(emp, classID);
            if (!exist) {
                rec.add(new Record(emp, classID, name, classNme));
            }
        }
    }

    private boolean isExist(String emp, String id) {
        for (Record record : rec) {
            if (record.getClassID().equals(id) && record.getEmp().equals(emp)) {
                return true;
            }
        }
        return false;
    }
}

