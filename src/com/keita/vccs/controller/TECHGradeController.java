package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.util.Utility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class TECHGradeController {

    @FXML private TreeTableView<ScoreTable> table;
    @FXML private TreeTableColumn<ScoreTable, String> empColumn;
    @FXML private TreeTableColumn<ScoreTable, String> cIDColumn;
    @FXML private TreeTableColumn<ScoreTable, String> nameColumn;
    @FXML private TreeTableColumn<ScoreTable, String> sNameColumn;
    @FXML private TreeTableColumn<ScoreTable, String> scoreColumn;
    @FXML private Button gExcel;

    private static ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private static ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private static ObservableList<ScoreTable> scoreTables = FXCollections.observableArrayList();
    private static ObservableList<Record> rec = FXCollections.observableArrayList();
    private static TreeItem<ScoreTable> scoreItem = new TreeItem<>(new ScoreTable("EMP ID", "CLASS ID", "NAME",
            "SCORE NAME", "SCORE"));

    @FXML
    public void initialize() {
        if (scoreTables.size() > 0) {
            scoreTables.clear();
            Utility.loadAllData(teachers, classes, students, TeacherController.userID, TeacherController.userType);
            scoreTableData();
            scoreItem.getChildren().clear();
            scoreItem = new TreeItem<>(new ScoreTable("EMP ID", "CLASS ID", "NAME",
                    "SCORE NAME", "SCORE"));
        }
        else {
            Utility.loadAllData(teachers, classes, students, TeacherController.userID, TeacherController.userType);
            scoreTableData();
        }
        gExcel.setOnAction(e -> {
//            method.exportStudentGrade();
        });

        view();
    }

    public void view() {
        TreeItem<ScoreTable> newScore;
        System.out.println("Teacher " + teachers.size() + " student " + students.size());

        for (ScoreTable sc : scoreTables) {
            newScore = new TreeItem<>(new ScoreTable(sc.getId(), sc.getClassID(),
                    sc.getName(), sc.getScoreName(), sc.getScore()));
            scoreItem.getChildren().add(newScore);
        }

        empColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScoreTable, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getId()));

        cIDColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScoreTable, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScoreTable, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        sNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScoreTable, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScoreName()));
        scoreColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<ScoreTable, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScore()));

        table.setRoot(scoreItem);
        table.setShowRoot(false);
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
