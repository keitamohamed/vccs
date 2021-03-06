package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.User;
import com.keita.vccs.sql.SQLStatement;
import com.keita.vccs.stage.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class AdminController extends LoginController{

    private static String empID, userType;

    @FXML private Button aClear, aSubmit;

    @FXML private Pane aHomePane, aNewUserPane, aNewClassPane;
    @FXML private TableView<User> aUserTable;
    @FXML private TableView<Class> aClassTable;
    @FXML private TextField aSearchField;

    // New user registration variables
    @FXML private TextField aFirstName, aLastName, aEmail, aPhoneNum, aAddress;
    @FXML private TextField aCity, aState, aZipcode, aUsername;
    @FXML private PasswordField aPassword, aConformPass;
    @FXML private DatePicker aDatePicker;
    @FXML private ChoiceBox aDropBox;

    // New Class registration variables
    @FXML private TextField aNewClassID, aNewClassName;
    @FXML private TextArea aNewClassDesc;

    @FXML private TableColumn<User, String> userID, name, email, phone, type;
    @FXML private TableColumn<Class, String> classID, className;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Class> classes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dashboard();
        if (users.size() == 0) {
            loadDate();
            loadDataInTable();
        }
        search();
    }

    private void loadDataInTable() {
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        type.setCellValueFactory(new PropertyValueFactory<>("userType"));

        if (aUserTable.getColumns().size() > 0 || aUserTable.getItems().size() > 0) {
            aUserTable.getItems().clear();
            aUserTable.getColumns().clear();
        }

        userID.setSortType(TableColumn.SortType.ASCENDING);
        aUserTable.setItems(users);
        aUserTable.getColumns().addAll(userID, name, email, phone, type);
        aUserTable.getSortOrder().add(userID);

        classID.setCellValueFactory(new PropertyValueFactory<>("classID"));
        className.setCellValueFactory(new PropertyValueFactory<>("className"));

        if (aClassTable.getColumns().size() > 0 || aClassTable.getItems().size() > 0) {
            aClassTable.getItems().clear();
            aClassTable.getColumns().clear();
        }

        classID.setSortType(TableColumn.SortType.ASCENDING);
        aClassTable.setItems(classes);
        aClassTable.getColumns().addAll(classID, className);
        aClassTable.getSortOrder().add(classID);
    }

    private void search () {
        FilteredList<User> filteredUserData = new FilteredList<>(users, p -> true);

        aSearchField.textProperty().addListener((observable, oldValue, newValue) -> filteredUserData.setPredicate(user -> {
            // If filter text is empty, display all persons.
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Compare first name and last name of every user with filter text.
            String lowerCaseFilter = newValue.toLowerCase();

            return user.getUserID().toLowerCase().contains(lowerCaseFilter) ||
                    user.getName().toLowerCase().contains(lowerCaseFilter) ||
                    user.getEmail().toLowerCase().contains(lowerCaseFilter) ||
                    user.getPhone().toLowerCase().contains(lowerCaseFilter) ||
                    user.getUserType().toLowerCase().contains(lowerCaseFilter);
        }));

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<User> sortedData = new SortedList<>(filteredUserData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(aUserTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        aUserTable.setItems(sortedData);
    }

    private void loadDate() {
        SQLStatement.loadDate(users, classes);
    }

    @FXML
    private void dashboard() {
        aHomePane.setVisible(true);
        aNewUserPane.setVisible(false);
        aNewClassPane.setVisible(false);
    }

    @FXML
    private void newUser() {
        aNewUserPane.setVisible(true);
        aHomePane.setVisible(false);
        aNewClassPane.setVisible(false);
    }

    @FXML void newClass() {
        aNewClassPane.setVisible(true);
        aHomePane.setVisible(false);
        aNewUserPane.setVisible(false);
    }

    @Override
    public void singOut(Event event) {
        StageManager.singOut(event);
    }

    public static void userID(String id, String type) {
        empID = id;
        userType = type;
    }
}
