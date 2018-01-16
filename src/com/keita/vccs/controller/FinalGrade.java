package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.message.Message;
import com.keita.vccs.sql.SQLStatement;
import com.keita.vccs.util.Utility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FinalGrade {

    private Message message = new Message();

    @FXML private TreeTableView<Record> table;
    @FXML private TreeTableColumn<Record, String> empColumn, cIDColumn, nameColumn;
    @FXML private TreeTableColumn<Record, String> cNameColumn;
    @FXML private TextField empColumnT, classColumnT, nameColumnT;
    @FXML private TextField  tTerm, cNameColumnT, gradeColumnT, number, unitesT;
    @FXML private ChoiceBox<String> fClassType;
    @FXML private DatePicker datePicker;
    @FXML private Button fSubmit;

    private static ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private static ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private static ObservableList<ScoreTable> score = FXCollections.observableArrayList();

    private ObservableList<String> classList = FXCollections.observableArrayList();

    private static TreeItem<Record> record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));

    @FXML
    public void initialize() {

        if (teachers.size() == 0) {
            Utility.loadAllData(teachers, classes, students, TeacherLoginController.userID, TeacherLoginController.userType);
            classTypeAddData();
            sortByClass(fClassType.getSelectionModel().getSelectedItem());
        }
        else {
            record.getChildren().clear();
            record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));
            classTypeAddData();
            sortByClass(fClassType.getSelectionModel().getSelectedItem());
        }

        fClassType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            record.getChildren().clear();
            clearTextField();
            record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));
            sortByClass(newValue);
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, old, newValue) -> {
            addScore();
            Utility.finalGradeText(score, newValue, empColumnT, classColumnT, nameColumnT, cNameColumnT, gradeColumnT);
        });

        number.textProperty().addListener((observable, oldValue, newValue) ->
                Utility.calculateGrade(empColumnT.getText(), classColumnT.getText(), newValue, gradeColumnT));

        fSubmit.setOnAction(e -> {
            if (isFilledOut() && datePicker.getValue() != null) {
                SQLStatement.addRecord(empColumnT.getText(), classColumnT.getText(), nameColumnT.getText(), cNameColumnT.getText(),
                        unitesT.getText(), gradeColumnT.getText(), tTerm.getText(),
                        Integer.toString(datePicker.getValue().getYear()));
            } else {
                Message.errorRequire("Field's Require", ("All filed's are require. " +
                        "Please make sure all field have a value."));
            }
        });
    }


    private void addScore() {
        for (Class cal : classes) {
            for (Student stud : cal.getStudent()) {
                for (Grade grade : stud.getGrades()) {
                    score.add(new ScoreTable(stud.getId(), cal.getClassID(), stud.getName(),
                            grade.getScoreName(), Integer.toString(grade.getScore())));
                }
            }
        }
    }

    @FXML
    private void sortByClass(String newValue) {
        TreeItem<Record> rec;

        for (Teacher teach : teachers) {
            for (Class cla : teach.getClasses()) {
                for (Student stud : cla.getStudent()) {
                    if (cla.getClassName().equals(newValue)) {
                        rec = new TreeItem<>(new Record(stud.getId(), cla.getClassID(), stud.getName(), cla.getClassName()));
                        record.getChildren().add(rec);
                    }
                    if (newValue.equals("Show All Students")) {
                        rec = new TreeItem<>(new Record(stud.getId(), cla.getClassID(), stud.getName(), cla.getClassName()));
                        record.getChildren().add(rec);
                    }
                }
            }
        }

        empColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getEmp()));
        cIDColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        cNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Record, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassName()));

        table.setRoot(record);
        table.setShowRoot(false);
    }

    private void classTypeAddData() {
        if (classList.size() > 0) {
            classList.clear();
        }
        classList.add("Show All Students");
        classes.forEach(e -> classList.add(e.getClassName()));
        fClassType.setItems(classList);
        fClassType.getSelectionModel().selectFirst();
    }

    @FXML
    private void clearTextField() {
        empColumnT.clear();
        classColumnT.clear();
        nameColumnT.clear();
        cNameColumnT.clear();
        gradeColumnT.clear();
    }

    @FXML
    private boolean isFilledOut () {
        return !empColumnT.getText().equals("") && !classColumnT.getText().equals("") && !nameColumnT.getText().equals("") &&
                !tTerm.getText().equals("") && !cNameColumnT.getText().equals("") && !gradeColumnT.getText().equals("") &&
                !unitesT.getText().equals("");
    }
}
