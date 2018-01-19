package com.keita.vccs.controller;

import com.keita.vccs.message.Notify;
import com.keita.vccs.stage.StageManager;
import com.keita.vccs.util.Export;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.Grade;
import com.keita.vccs.blueprint.Student;
import com.keita.vccs.blueprint.Teacher;
import com.keita.vccs.sql.SQLStatement;
import com.keita.vccs.util.Utility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class TeacherController extends LoginController {
    public static String userID, userType;

    @FXML private Label empL, scoreNL, nameL, scoreL, classIDL, titleGA, classAvg;
    @FXML private TextField empT, scoreNT, scoreT, nameT, classIDT, searchField;
    @FXML private TextArea updateReason;
    @FXML private Button submitB, gradeB, updateB, assignmentB, exportExcel;
    @FXML private GridPane scoreAUGridPane;
    @FXML private TreeTableView<Student> table;
    @FXML private TreeTableColumn<Student, String> idColumn;
    @FXML private TreeTableColumn<Student, String> nColumn;
    @FXML private TreeTableColumn<Student, String> eColumn;
    @FXML private TreeTableColumn<Student, String> pColumn, classColumn;
    @FXML private ChoiceBox<String> tClassList;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private ObservableList<String> techLClass = FXCollections.observableArrayList();
    private TreeItem<Student> tTableRoot = new TreeItem<>(new Student("ID", "Class ID", "Name",
            "Email", "Phone"));

    private List<String> listScoreName = new ArrayList<>();


    @FXML
    public void initialize() {
        gradeB.setOnAction(e -> {
            titleGA.setText("ADD NEW GRADE FOR STUDENT");
            disableAFieldAndTextF();
        });
        updateB.setOnAction(e -> updateGrade());
        assignmentB.setOnAction(e -> {
            updateReason.setText("");
            updateReason.setPromptText("Enter assignment description " +
                    "and what to expect from student and what to expect as " +
                    "a finish work.");
            addAssignment();
        });

        submitB.setOnAction(e -> {
            addUpdate();
            loadDate();
        });

        exportExcel.setOnAction(e -> Export.exportListStudent(classes));

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (empT.isVisible()) {
                    empT.setText(newValue.getValue().getId());
                    classIDT.setText(newValue.getValue().getClassID());
                }
            }
        });

        disableAFieldAndTextF();
        cBoxActionListener();
    }

    @FXML
    private void loadDate() {
        clearTable();
        load();
        tClassList.setItems(techLClass);
        tClassList.getSelectionModel().selectFirst();
        classAvg(tClassList.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void cBoxActionListener() {
        tClassList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tTableRoot.getChildren().clear();
            tTableRoot = new TreeItem<>(new Student("ID", "Class ID", "Name",
                    "Email", "Phone"));
            sortBy(newValue);
            classAvg(newValue);
        });

        tClassList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends
                String> obs, String old, String newValue) ->
                classAvg(newValue));
    }

    private void load() {

        Utility.loadAllData(teachers, classes, students, userID, userType);

        TreeItem<Student> stud;
        tTableRoot.getChildren().clear();

        for (Class cal : classes) {
            for (Student student : cal.getStudent()) {
                stud = new TreeItem<>(new Student(student.getId(), student.getClassID(),
                        student.getName(), student.getEmail(), student.getPhone()));
                tTableRoot.getChildren().add(stud);
            }
        }

        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getId()));
        classColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        nColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        eColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getEmail()));
        pColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getPhone()));

        table.setRoot(tTableRoot);
        table.setShowRoot(false);

        if (techLClass.size() > 0) {
            techLClass.clear();

            techLClass.add("Show All Students");
            for (Class cal : classes) {
                techLClass.add(cal.getClassName());
            }
        } else {
            techLClass.add("Show All Students");
            for (Class cal : classes) {
                techLClass.add(cal.getClassName());
            }
        }
    }

    private void classAvg(String sClass) {
        Class cal = getClass(classes, sClass);
        displayAvg(cal, classAvg);
    }

    private void sortBy(String value) {

        TreeItem<Student> stud;

        for (Class cal : classes) {
            for (Student student : cal.getStudent()) {
                if (value != null) {
                    if (value.equals("Show All Students")) {
                        stud = new TreeItem<>(new Student(student.getId(), student.getClassID(),
                                student.getName(), student.getEmail(), student.getPhone()));
                        tTableRoot.getChildren().add(stud);
                    }

                    if (cal.getClassName().equals(value)) {
                        stud = new TreeItem<>(new Student(student.getId(), student.getClassID(),
                                student.getName(), student.getEmail(), student.getPhone()));
                        tTableRoot.getChildren().add(stud);
                    }
                }
            }
        }

        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getId()));
        classColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        nColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        eColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getEmail()));
        pColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Student, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getPhone()));

        table.setRoot(tTableRoot);
        table.setShowRoot(false);
    }

    private void addUpdate() {
        if (updateReason.isVisible() && !updateReason.getText().isEmpty() &&
                !classIDT.getText().isEmpty()) {
            String sName = updateStudentScore(empT, classIDT, scoreNT, scoreT, updateReason);

            String message = sName + " " + scoreNT.getText().trim() + " has been updated successful";
            Notify.successful("Successful Updated", message);

        }
        if (updateReason.isVisible() && updateReason.getText().trim().isEmpty()) {
            Notify.errorRequire("Need-Reason", "Sorry, you must " +
                    "provide a reason for this update");
        }

        if (!updateReason.isVisible() && !scoreNT.getText().trim().isEmpty()) {
            createScoreName(listScoreName);
            String scoreName = getScoreName();

            if (scoreName != null) {
                if (!alreadyExist(empT, scoreName)) {
                    scoreNT.setText(scoreName);
                    addScore(empT, classIDT, scoreNT, scoreT);
                    String message = scoreName + " has been successfully added for student id# " +
                            "" + empT.getText().trim();
                    Notify.errorRequire("Grade Added", message);
                } else {
                    String message = "Sorry could add score because " + scoreNT.getText().trim() + " " +
                            "already exist for student id/emp# " + empT.getText().trim();
                    Notify.errorRequire("Not-Added", message);
                }
            } else {
                String message = "Sorry could add score. You must enter a valid name, " +
                        "such as Home Work 1, Test 1, Mid term or Exam.";
                Notify.errorRequire("Not-Added", message);
            }
        }
    }

    private String updateStudentScore(TextField emp, TextField classID, TextField name,
                                      TextField score, TextArea reason) {
        String sName = null;
        for (Student stud : students) {
            if (stud.getId().equals(emp.getText().trim())) {
                sName = stud.getName();
                for (Grade grade : stud.getGrades()) {
                    if (grade.getScoreName().equalsIgnoreCase(name.getText().trim())) {
                        name.setText(grade.getScoreName());
                    }
                }
            }
        }
        SQLStatement.updateStudentScore(emp, classID, name, score, reason);
        return sName;
    }

    private void createScoreName(List<String> scoreName) {
        String[] name = {"Home Work", "Test", "Mid Term", "Exam"};
        for (int i = 0; i < name.length; i++) {
            if (name[i].equals("Home Work") || name[i].equals("Test")) {
                for (int j = 1; j < 5; j++) {
                    scoreName.add(name[i] + " " + j);
                }
            }
            scoreName.add(name[i] + " " + i);
        }
    }

    private String getScoreName() {
        for (String name : listScoreName) {
            if (name.equalsIgnoreCase(scoreNT.getText().trim())) {
                return name;
            }
        }
        return null;
    }

    private Class getClass(ObservableList<Class> c, String className) {
        for (Class cal : c) {
            if (cal.getClassName().equals(className)) {
                return cal;
            }
        }
        return null;
    }

    private void displayAvg(Class cla, Label setScore) {
        int totalScore = 0, count = 0;
        if (cla != null) {
            for (Student stud : cla.getStudent()) {
                for (Grade score : stud.getGrades()) {
                    if (score.getScore() <= 10) {
                        totalScore += (score.getScore() * 10);
                        count++;
                    } else {
                        totalScore += score.getScore();
                        count++;
                    }
                }
            }
        }
        if (cla == null) {
            setScore.setText("");
        } else if (cla.getClassName() != null && totalScore == 0) {
            setScore.setText(cla.getClassName() + ": No Grade");
        } else {
            setScore.setText(cla.getClassName() + ": " + (totalScore / count) + "%");
        }
    }

    private boolean alreadyExist(TextField emp, String name) {
        for (Student stud : students) {
            if (stud.getId().equals(emp.getText().trim())) {
                for (Grade grade : stud.getGrades()) {
                    if (grade.getScoreName().equalsIgnoreCase(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addScore(TextField emp, TextField classID, TextField name,
                          TextField score) {
        SQLStatement.addGrade(emp, classID, name, score);
    }

    @FXML
    private void disableAFieldAndTextF() {
        GridPane.setRowIndex(submitB, 4);
        empL.setVisible(true);
        empT.setVisible(true);
        scoreNT.setVisible(true);
        scoreNL.setVisible(true);
        scoreL.setVisible(true);
        scoreT.setVisible(true);
        nameL.setVisible(false);
        nameT.setVisible(false);
        classIDL.setVisible(true);
        classIDT.setVisible(true);
        updateReason.setVisible(false);
        gridPercentHeight(scoreAUGridPane);
    }

    @FXML
    private void updateGrade () {
        updateB.setOnAction(e -> {
            titleGA.setText("UPDATE STUDENT OLD GRADE");
            disableAFieldAndTextF();
            updateReason.setVisible(true);
            updateReason.setPromptText("Enter reason for this update.");
            updateReason.setText("");
            GridPane.setRowIndex(updateReason, 4);
            GridPane.setRowIndex(submitB, 5);
            gridPercentHeight(scoreAUGridPane);
            scoreAUGridPane.getRowConstraints().get(4).setPercentHeight(25);
            updateReason.setMinHeight(20);
        });
    }

    @FXML
    private void addAssignment () {
        titleGA.setText("NEW ASSIGNMENT FOR STUDENT");
        nameL.setVisible(true);
        nameT.setVisible(true);
        updateReason.setVisible(true);
        GridPane.setRowIndex(nameL, 0);
        GridPane.setColumnIndex(nameL, 0);
        GridPane.setRowIndex(nameT, 0);
        GridPane.setRowIndex(updateReason, 1);
        GridPane.setColumnIndex(updateReason, 1);

        GridPane.setRowIndex(submitB, 2);
        gridPercentHeight(scoreAUGridPane);
        scoreAUGridPane.getRowConstraints().get(1).setPercentHeight(25);
        updateReason.setMinHeight(20);
        disableGradeField();

    }

    @FXML
    private void disableGradeField () {
        empL.setVisible(false);
        empT.setVisible(false);
        classIDT.setVisible(false);
        classIDL.setVisible(false);
        scoreNL.setVisible(false);
        scoreNT.setVisible(false);
        scoreL.setVisible(false);
        scoreT.setVisible(false);
    }

    @FXML
    private void clearTable() {
        if (tTableRoot.getChildren().size() > 0 || table.getColumns() != null) {
            tTableRoot.getChildren().clear();
            tTableRoot = new TreeItem<>(new Student("ID", "Class ID", "Name",
                    "Email", "Phone"));
        }
    }

    @FXML
    private void gridPercentHeight(GridPane gridPane) {
        int row = gridPane.getRowConstraints().size();
        for (int i = 0; i < row; i++) {
            gridPane.getRowConstraints().get(i).setPercentHeight(10);
        }
    }

    @Override
    public void singOut(Event event) {
        StageManager.singOut(event);
    }

    @FXML
    public void gradeFxml()  {
        StageManager.switchScene("../fxml/TECHStudentGrade.fxml", "List Of Student's And Grade");
    }

    @FXML
    public void finalGradeFxml()  {
        StageManager.switchScene("../fxml/FinalGrade.fxml", "Student Final Grade");
    }

    public static void userID(String id, String type) {
        userID = id;
        userType = type;
    }
}
