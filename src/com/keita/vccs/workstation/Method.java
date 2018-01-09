package com.keita.vccs.workstation;

import com.keita.vccs.associate.Export;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.controller.StudentController;
import com.keita.vccs.message.Message;
import com.keita.vccs.sqlstatement.SQLStatement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Method extends Calculation {

    private Message msg = new Message();
    private Validation validation = new Validation();
    private static Message ms = new Message();
    private Export export = new Export();

    private ObservableList<Teacher> teachers;
    private ObservableList<Class> classes;
    private ObservableList<Student> students;
    private ObservableList<Record> records = FXCollections.observableArrayList();
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

    private void scoreTableData() {
        for (Teacher teach : teachers) {
            for (Class cal : teach.getClasses()) {
                for (Student stud : cal.getStudent()) {
                    addRecord(stud.getId(), cal.getClassID(), stud.getName(), cal.getClassName());
                    for (Grade grade : stud.getGrades()) {
                        scoreTables.add(new ScoreTable(stud.getId(), cal.getClassID(),
                                stud.getName(), grade.getScoreName(), Integer.toString(grade.getScore())));
                    }
                }
            }
        }
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

    public boolean doClassExist(String id) {
        return validation.isClassExist(classes, id);
    }
}

