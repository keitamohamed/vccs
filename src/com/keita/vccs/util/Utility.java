package com.keita.vccs.util;

import com.keita.vccs.associate.Export;
import com.keita.vccs.associate.OtherClasses;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.controller.StudentController;
import com.keita.vccs.message.Message;
import com.keita.vccs.sqlstatement.SQLStatement;
import com.keita.vccs.workstation.Calculation;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class Utility extends Calculation{
    private Message msg = new Message();
    private Validation validation = new Validation();
    private static Message ms = new Message();
    private Export export = new Export();

    private static SQLStatement statements = new SQLStatement();

    private static ObservableList<Teacher> teachers = FXCollections.observableArrayList();;
    private static ObservableList<Class> classes = FXCollections.observableArrayList();;
    private ObservableList<Student> students = FXCollections.observableArrayList();;
    private static ObservableList<Student> student = FXCollections.observableArrayList();
    private static ObservableList<ScoreTable> scoreTables = FXCollections.observableArrayList();
    private static ObservableList<Record> rec = FXCollections.observableArrayList();

    public static ObservableList<OtherClasses> loadAllData(ObservableList<Teacher> teachers, ObservableList<Class> classes,
                                   ObservableList<Student> students, String userID, String userType) {

        ObservableList<OtherClasses> otherClasses = statements.loadDate(teachers, classes, students, userID, userType);
        return otherClasses;
    }

    private SQLStatement statement = new SQLStatement();
    private static TreeItem<ScoreTable> scoreItem = new TreeItem<>(new ScoreTable("EMP ID", "CLASS ID", "NAME",
            "SCORE NAME", "SCORE"));

    public Utility() {
        teachers = FXCollections.observableArrayList();
        classes = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
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
            ms.alert("Number Format Exception", "Please enter a number. " +
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
            ms.alert("Number Format Exception", "Please enter a number. " +
                    "Not a String or character.");
        }
    }

    public static void finalGradeTable(TreeTableView<Record> treeView, TreeItem<Record>
            record, TreeTableColumn<Record, String> emp, TreeTableColumn<Record, String>
                                               classID, TreeTableColumn<Record, String> name, TreeTableColumn<Record, String>
                                               className, ChoiceBox<String> classType, ObservableList<String> classList) {

        if (classList.size() == 0) {
            classList.add("Show All Students");
            for (Record rd : rec) {
                if (classList.contains(rd.getClassName())) {
                    continue;
                }
                classList.add(rd.getClassName());
            }

            classType.setItems(classList);
            classType.getSelectionModel().selectFirst();
        }

        TreeItem<Record> newScore;

        for (Record rc : rec) {
            if (classType.getValue().equals("Show All Students")) {
                newScore = new TreeItem<>(new Record(rc.getEmp(), rc.getClassID(),
                        rc.getName(), rc.getClassName()));
                record.getChildren().add(newScore);
            }

            if (classType.getValue().equals(rc.getClassName())) {
                newScore = new TreeItem<>(new Record(rc.getEmp(), rc.getClassID(),
                        rc.getName(), rc.getClassName()));
                record.getChildren().add(newScore);
            }
        }

        emp.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getEmp()));

        classID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        name.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        className.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassName()));

        treeView.setRoot(record);
        treeView.setShowRoot(false);
    }

    public static void finalGradeText(TreeItem<Record> newValue, TextField emp, TextField classID, TextField name, TextField className, TextField letterGrade) {
        if (newValue != null) {
            emp.setText(newValue.getValue().getEmp());
            classID.setText(newValue.getValue().getClassID());
            name.setText(newValue.getValue().getName());
            className.setText(newValue.getValue().getClassName());
            int point = Calculation.addScore(scoreTables, newValue.getValue().getEmp(), newValue.getValue().getClassID());
            letterGrade.setText(Calculation.calculateLetterGrade(point));
        }
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
