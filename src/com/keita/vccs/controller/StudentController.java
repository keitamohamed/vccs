package com.keita.vccs.controller;

import com.keita.vccs.associate.Associate;
import com.keita.vccs.associate.OtherClasses;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.message.Message;
import com.keita.vccs.util.Utility;
import com.keita.vccs.workstation.Method;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class StudentController extends Controller{
    public static String userID, userType;
    private Method method = new Method();
    private Associate associate = new Associate();
    private Message msg = new Message();

    @FXML private Label sEmpL, sTechEMPL, sClassIDL, sAssNameL, grade, titleGA;
    @FXML private TextField empT, techIDT, classIDT, aNameT;
    @FXML private TextArea assignment;
    @FXML private Button addClass, assignmentB, choseFile, submitB;
    @FXML private GridPane registerPane;
    @FXML private TreeTableView<StudentT> table;
    @FXML private TreeTableColumn<StudentT, String> tClassID, tCName, tScore;
    @FXML private ChoiceBox<String> sortBy;
    @FXML private TableView<OtherClasses> tableView;
    @FXML private TableColumn<OtherClasses, String> classID, className;

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private ObservableList<Class> classes = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private ObservableList<OtherClasses> otherClass = FXCollections.observableArrayList();


    private ObservableList<String> studentClasses = FXCollections.observableArrayList();
    private TreeItem<StudentT> studentTreeItem = new TreeItem<>(new StudentT("Class ID", "Score Name", "Score"));

    @FXML
    public void initialize() {

        disableAddAssignment();
        sortBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            studentTreeItem.getChildren().clear();
            studentTreeItem = new TreeItem<>(new StudentT("Class ID", "Score Name", "Score"));
            sortBy(newValue);
            Utility.calculateGrade(classes, sortBy.getSelectionModel().getSelectedItem(), grade) ;
        });

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
                classIDT.setText(newValue.getValue().getClassID());
            }
        });

        submitB.setOnAction(e -> {
            if (!empT.getText().equals("") && !techIDT.getText().equals("") &&
                    !classIDT.getText().equals("") && !method.doClassExist(classIDT.getText())) {
                method.registerForClass(empT.getText(), techIDT.getText(),
                        classIDT.getText(), aNameT);
                loadData();
                msg.alert("Successfully Added", (classIDT.getText() + " was " +
                        "successfully added to your class list"));
            }
            else {
                msg.alert("Not Added", (classIDT.getText() + " was not added to your class " +
                        "list because you are\nalready resisted in this class."));
            }
        });
    }

    @FXML
    private void loadData() {
        load();
        sortBy.setItems(studentClasses);
        sortBy.getSelectionModel().selectFirst();
        tableView();
    }

    private void load() {

        otherClass = Utility.loadAllData(teachers, classes, students, userID, userType);

        TreeItem<StudentT> stud;

        if (studentClasses.size() > 0) {
            studentClasses.clear();
        }
        studentClasses.add("Show All Grade");

        for (Teacher teacher : teachers) {
            for (Class cal : teacher.getClasses()) {
                studentClasses.add(cal.getClassName());
                for (Student student : cal.getStudent()) {
                    for (Grade grade : student.getGrades()) {
                        stud = new TreeItem<>(new StudentT(cal.getClassID(), grade.getScoreName(), Integer.toString(grade.getScore())));
                        studentTreeItem.getChildren().add(stud);
                    }
                }
            }
        }

        tClassID.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        tCName.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScoreName()));
        tScore.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScore()));

        table.setRoot(studentTreeItem);
        table.setShowRoot(false);
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

    private void sortBy(String sortBy) {

        TreeItem<StudentT> stud;

        for (Class cal : classes) {
            for (Student student : cal.getStudent()) {
                for (Grade grade : student.getGrades()) {
                    if (sortBy != null) {
                        if (sortBy.equals("Show All Grade")) {
                            stud = new TreeItem<>(new StudentT(cal.getClassID(), grade.getScoreName(), Integer.toString(grade.getScore())));
                            studentTreeItem.getChildren().add(stud);
                        }

                        if (sortBy.equals(cal.getClassName())) {
                            stud = new TreeItem<>(new StudentT(cal.getClassID(), grade.getScoreName(), Integer.toString(grade.getScore())));
                            studentTreeItem.getChildren().add(stud);
                        }
                    }
                }
            }
        }

        tClassID.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getClassID()));
        tCName.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScoreName()));
        tScore.setCellValueFactory((TreeTableColumn.CellDataFeatures<StudentT, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getScore()));

        table.setRoot(studentTreeItem);
        table.setShowRoot(false);
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
        associate.changeStage("../fxml/GPA.fxml", "Student GPA");
    }

    @FXML
    private void courseHistoryFXM() {
        associate.changeStage("../fxml/CourseHistory.fxml", "Course History");
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
        associate.singOut(event);
    }

    public static void userID(String id, String type) {
        userID = id;
        userType = type;
    }
}
