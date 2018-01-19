package com.keita.vccs.util;

import com.keita.vccs.blueprint.OtherClasses;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.controller.StudentController;
import com.keita.vccs.message.Notify;
import com.keita.vccs.sql.SQLStatement;
import com.keita.vccs.calculate.Calculation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class Utility extends Calculation{
    private Notify msg = new Notify();
    private static Notify ms = new Notify();
    private Export export = new Export();

    private static SQLStatement statements = new SQLStatement();

    private static ObservableList<Student> student = FXCollections.observableArrayList();
    private static ObservableList<ScoreTable> scoreTables = FXCollections.observableArrayList();
    private static ObservableList<Record> rec = FXCollections.observableArrayList();

    public static ObservableList<OtherClasses> loadAllData(ObservableList<Teacher> teachers, ObservableList<Class> classes,
                                   ObservableList<Student> students, String userID, String userType) {

        ObservableList<OtherClasses> otherClasses = statements.loadDate(teachers, classes, students, userID, userType);
        return otherClasses;
    }

    public static void loadStudentInfo() {
        SQLStatement.getStudentInfo(student, StudentController.userID);
    }

    public static void studentInfo(Label emp, Label name, Label email, Label num) {
        for (Student stud : student) {
            emp.setText(stud.getId().toUpperCase());
            name.setText(stud.getName().toUpperCase());
            email.setText(stud.getEmail().toUpperCase());
            num.setText(stud.getPhone().toUpperCase());
        }
    }

    public static void calculateGrade(String emp, String classID, String totalNum,
                                      TextField letterGrade) {
        try {
            int number = 0;
            if (totalNum.length() > 0) {
                number = Integer.parseInt(totalNum);
            }
            int grade = Calculation.addScore(scoreTables, emp, classID, number);
            letterGrade.setText(calculateLetterGrade(grade));

        }catch (NumberFormatException nf) {
            Notify.errorRequire("Number Format Exception", "Please enter a number. " +
                    "Not a String or character.");
        }

    }

    public static void calculateGrade(ObservableList<Class> classes, String className, Label letterGrade) {
        try {
            int grade = Calculation.addScore(classes, className);
            if (grade != -1) {
                letterGrade.setText(className + ": " + calculateLetterGrade(grade));
            }else {
                letterGrade.setText(className + ": No Grade" );
            }

        }catch (NumberFormatException nf) {
            Notify.errorRequire("Number Format Exception", "Please enter a number. " +
                    "Not a String or character.");
        }
    }

    public static void finalGradeText(ObservableList<ScoreTable> scoreTables, TreeItem<Record> newValue, TextField emp, TextField classID, TextField name, TextField className, TextField letterGrade) {
        if (newValue != null) {
            emp.setText(newValue.getValue().getEmp());
            classID.setText(newValue.getValue().getClassID());
            name.setText(newValue.getValue().getName());
            className.setText(newValue.getValue().getClassName());
            int point = Calculation.addScore(scoreTables, newValue.getValue().getEmp(), newValue.getValue().getClassID());
            letterGrade.setText(Calculation.calculateLetterGrade(point));
        }
    }

    public void exportStudentGrade() {
        export.exportStudentGrade(scoreTables);
        Notify.errorRequire("Successfully Exported", "Date was exported successfully as an excel file");
    }

    private static void addRecord(String emp, String classID, String name, String classNme) {
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

    private static boolean isExist(String emp, String id) {
        for (Record record : rec) {
            if (record.getClassID().equals(id) && record.getEmp().equals(emp)) {
                return true;
            }
        }
        return false;
    }
}
