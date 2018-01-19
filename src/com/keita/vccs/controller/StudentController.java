package com.keita.vccs.controller;

import com.keita.vccs.stage.StageManager;
import com.keita.vccs.blueprint.OtherClasses;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.message.Notify;
import com.keita.vccs.sql.SQLStatement;
import com.keita.vccs.util.Utility;
import com.keita.vccs.util.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class StudentController extends LoginController {
    public static String userID, userType;

    @FXML private Label sEmpL, sTechEMPL, sClassIDL, sAssNameL, titleGA;
    @FXML private TextField empT, techIDT, classIDT, aNameT, search;
    @FXML private TextArea assignment;
    @FXML private Button addClass, assignmentB, choseFile, submitB;
    @FXML private GridPane registerPane;
    @FXML private TableView<Grade> table;
    @FXML private TableColumn<Grade, String> tClassID, tCName;
    @FXML private TableColumn<Grade, Number> tScore;
    @FXML private TableView<OtherClasses> tableView;
    @FXML ChoiceBox<String> sortBy;
    @FXML private TableColumn<OtherClasses, String> classID, className;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private ObservableList<OtherClasses> otherClass = FXCollections.observableArrayList();
    private ObservableList<Grade> scores = FXCollections.observableArrayList();

    private ObservableList<String> studentClasses = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        disableAddAssignment();

        addClass.setOnAction(e -> {
            disableAddAssignment();
            titleGA.setText("REGISTER FOR CLASS");
            classIDT.setText("");
            registerPane.getRowConstraints().get(0).setPercentHeight(15);
            registerPane.getRowConstraints().get(1).setPercentHeight(15);
            registerPane.getRowConstraints().get(2).setPercentHeight(15);
            registerPane.getRowConstraints().get(3).setPercentHeight(15);
        });

        assignmentB.setOnAction(e -> disableAddClassField());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!aNameT.isVisible() && newValue != null) {
                empT.setText(StudentController.userID);
                classIDT.setText(newValue.getClassID());
                techIDT.setText(newValue.getEmp());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (aNameT.isVisible() && newValue != null) {
                classIDT.setText(newValue.getClassID());
            }
        });

        submitB.setOnAction(e -> {
            if (!empT.getText().equals("") && !techIDT.getText().equals("") &&
                    !classIDT.getText().equals("") && !Validation.classExist(classes, classIDT.getText())) {
                register(empT.getText(), techIDT.getText(), classIDT.getText(), aNameT);
                loadData();
                Notify.errorRequire("Successfully Added", (classIDT.getText() + " was " +
                        "successfully added to your class list"));
            }
            else {
                Notify.errorRequire("Not Added", (classIDT.getText() + " was not added to your class " +
                        "list because you are\nalready resisted in this class."));
            }
        });

        sortBy();
    }

    @FXML
    private void loadData() {
        load();
        tableView();
        sortBy.setItems(studentClasses);
        sortBy.getSelectionModel().selectFirst();
    }

    private void load() {

        otherClass = Utility.loadAllData(teachers, classes, students, userID, userType);

        if (table.getColumns().size() > 0 || table.getItems().size() > 0) {
            table.getItems().clear();
            table.getColumns().clear();
        }

        tClassID.setCellValueFactory(new PropertyValueFactory<>("classID"));
        tCName.setCellValueFactory(new PropertyValueFactory<>("scoreName"));
        tScore.setCellValueFactory(new PropertyValueFactory<>("score"));

        if (studentClasses.size() > 0) {
            studentClasses.clear();
        }
        studentClasses.add("Show all student");

        for (Teacher teacher : teachers) {
            for (Class cal : teacher.getClasses()) {
                studentClasses.add(cal.getClassName());
                for (Student student : cal.getStudent()) {
                    for (Grade grade : student.getGrades()) {
                        scores.add(new Grade(cal.getClassID(), grade.getScoreName(), grade.getScore()));
                    }
                }
            }
        }

        table.setItems(scores);
        table.getColumns().addAll(tClassID, tCName, tScore);
    }

    private void tableView() {

        classID.setCellValueFactory(new PropertyValueFactory<>("classID"));
        className.setCellValueFactory(new PropertyValueFactory<>("className"));

        classID.setPrefWidth(100);
        className.setPrefWidth(258);

        if (tableView.getColumns().size() > 0 || tableView.getItems().size() > 0) {
            tableView.getItems().clear();
            tableView.getColumns().clear();
        }
        tableView.setItems(otherClass);
        tableView.getColumns().add(classID);
        tableView.getColumns().add(className);
    }

    private void sortBy() {

        FilteredList<Grade> filteredScoreData = new FilteredList<>(scores, p -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredScoreData.setPredicate(score -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every user with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                return score.getClassID().toLowerCase().contains(lowerCaseFilter) ||
                        score.getScoreName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Grade> sortedData = new SortedList<>(filteredScoreData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
    }

    private void register(String emp, String techEMP, String classID, TextField aNameT) {
        if (!aNameT.isVisible()) {
            SQLStatement.registerForClass(emp, techEMP, classID);
        }else {
            // Assignment code go here

        }
    }

    @FXML
    private void disableAddClassField() {
        titleGA.setText("SUBMIT ASSIGNMENT");
        sClassIDL.setVisible(true);
        classIDT.setVisible(true);
        sAssNameL.setVisible(true);
        aNameT.setVisible(true);
        choseFile.setVisible(true);
        assignment.setVisible(true);
        classIDT.setText("");

        GridPane.setRowIndex(sClassIDL, 0);
        GridPane.setColumnIndex(sClassIDL, 0);
        GridPane.setRowIndex(classIDT, 0);
        GridPane.setColumnIndex(classIDT, 1);
        GridPane.setRowIndex(sAssNameL, 1);
        GridPane.setColumnIndex(sAssNameL, 0);
        GridPane.setRowIndex(aNameT, 1);
        GridPane.setColumnIndex(aNameT, 1);
        GridPane.setRowIndex(choseFile, 2);
        GridPane.setRowIndex(assignment, 3);
        GridPane.setColumnIndex(assignment, 1);
        GridPane.setRowIndex(submitB, 4);
        GridPane.setColumnIndex(submitB, 1);

        gridPercentHeight(registerPane);

        registerPane.getRowConstraints().get(3).setPercentHeight(30);
        assignment.setMinHeight(30);

        sEmpL.setVisible(false);
        sTechEMPL.setVisible(false);
        empT.setVisible(false);
    }

    @FXML
    private void disableAddAssignment() {
        sEmpL.setVisible(true);
        sTechEMPL.setVisible(true);
        sClassIDL.setVisible(true);
        empT.setVisible(true);
        techIDT.setVisible(true);
        classIDT.setVisible(true);

        GridPane.setRowIndex(sClassIDL, 2);
        GridPane.setColumnIndex(sClassIDL, 0);
        GridPane.setRowIndex(classIDT, 2);
        GridPane.setColumnIndex(classIDT, 1);
        GridPane.setRowIndex(submitB, 3);
        GridPane.setColumnIndex(submitB, 1);

        registerPane.getRowConstraints().get(0).setPercentHeight(13);
        registerPane.getRowConstraints().get(1).setPercentHeight(13);
        registerPane.getRowConstraints().get(2).setPercentHeight(13);
        registerPane.getRowConstraints().get(3).setPercentHeight(13);

        sAssNameL.setVisible(false);
        aNameT.setVisible(false);
        choseFile.setVisible(false);
        assignment.setVisible(false);
    }

    @FXML
    private void profileFXM() {
        StageManager.switchScene("../fxml/GPA.fxml", "Student GPA");
    }

    @FXML
    private void courseHistoryFXM() {StageManager.switchScene("../fxml/CourseHistory.fxml", "Course History");
    }

    @FXML
    private void gridPercentHeight(GridPane gridPane) {
        int row = gridPane.getRowConstraints().size();
        for (int i = 0; i < row; i++) {
            gridPane.getRowConstraints().get(i).setPercentHeight(15);
        }
    }

    @FXML
    @Override
    public void singOut(Event event) {
        StageManager.singOut(event);
    }

    public static void userID(String id, String type) {
        userID = id;
        userType = type;
    }
}
