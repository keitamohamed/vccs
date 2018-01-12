package com.keita.vccs.controller;

import com.keita.vccs.connection.MySQLConnection;
import com.keita.vccs.message.Message;
import com.keita.vccs.sql.SQLStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    private Message message = new Message();
    private SQLStatement statement = new SQLStatement();

    private Connection connect = null;
    private Statement stmt = null;
    private MySQLConnection mySQL = new MySQLConnection();

    @FXML
    private ChoiceBox<String> userType;
    @FXML
    private TextField lUsernameT;
    @FXML
    private PasswordField lPasswordT;
    @FXML
    private Button clearL;

    ObservableList<String> type = FXCollections.observableArrayList("Admin",
            "Teacher", "Student");


    @FXML
    public void initialize() {
        userType.setItems(type);
        userType.getSelectionModel().select(1);
        clearL.setOnAction(e -> clearLF());
    }

    @FXML
    public void stage(Event event) throws ClassNotFoundException, IOException{
        userType.setItems(type);
        switch (userType.getValue()) {
            case "Admin":
                userStage(event, "../fxml/Admin.fxml", "Admin Dashboard");
                break;
            case "Teacher":
                userStage(event, "../fxml/Teacher.fxml", "Teacher Dashboard");
                break;
            case "Student":
                userStage(event, "../fxml/Student.fxml", "Student Dashboard");
        }
    }

    @FXML
    public void userStage(Event event, String fxml, String title) throws ClassNotFoundException, IOException {
        String userID = checkLogin(lUsernameT.getText().trim(),
                lPasswordT.getText().trim());
        if (isFiledOut() && connectCheck() != null && userID != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                AnchorPane teacherPane = loader.load(getClass().getResource(fxml));
                sendID(userID, userType.getSelectionModel().getSelectedItem());
                loader.setRoot(teacherPane);
                Scene tScene = new Scene(teacherPane);
                Stage tStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                tStage.setScene(tScene);
                tStage.setTitle(title);
                tStage.setResizable(false);
                tStage.getScene().getStylesheets().add(getClass().getResource("../css/Table.css").toExternalForm());
                tStage.getScene().getStylesheets().add(getClass().getResource("../css/Main.css").toExternalForm());
                tStage.getScene().getStylesheets().add(getClass().getResource("../css/Message.css").toExternalForm());
                tStage.show();
            }
            catch (IOException io) {
                io.printStackTrace();
                System.out.println("IO-Exception " + io.fillInStackTrace());
            }
        }
    }

    private void sendID(String userID, String userType) {
        if (userType.equals("Teacher")) {
            TeacherController.userID(userID, userType);
        }
        else if (userID.equals("Admin")) {

        }else {
            StudentController.userID(userID, userType);
        }
    }


    @FXML
    public void singOut(Event event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    @FXML
    private boolean isFiledOut() {

        if (!lUsernameT.getText().isEmpty() && !
                lPasswordT.getText().isEmpty()) {
            return true;
        }
        String eMessage = "You must filed out all filed.";
        Message.errorRequire("Filed Require", eMessage);
        return false;
    }

    @FXML
    public Connection connectCheck() throws ClassNotFoundException, IOException{

        if (connect == null) {
            connect = mySQL.mysql();
        }

        if (stmt == null) {
            try {
                stmt = connect.createStatement();
            }
            catch (SQLException | NullPointerException e) {
            }
        }
        return connect;
    }

    public String checkLogin(String username, String password) {
        if (statement.login(username, password, userType) != null) {
            return statement.login(username, password, userType);
        }

        String message = "Username: " + username + ", password " + password +
                " user type " + userType.getValue() + " is in correct. " +
                "Make sure all information are correct.";
        this.message.notification("No-Match", message);
        return null;
    }

    @FXML
    public void clearLF() {
        lUsernameT.clear();
        lPasswordT.clear();
    }
}
