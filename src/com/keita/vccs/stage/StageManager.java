package com.keita.vccs.stage;

import com.keita.vccs.controller.StudentController;
import com.keita.vccs.controller.TeacherController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {

    @FXML
    public static void switchStage(Event event, String fxml, String userID, String userType, String title) {
        try {
            AnchorPane root = FXMLLoader.load(StageManager.class.getResource(fxml));
            sendID(userID, userType);
            Scene tScene = new Scene(root);
            Stage tStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            tStage.setScene(tScene);
            tStage.setTitle(title);
            tStage.setResizable(false);
            tStage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Admin.css").toExternalForm());
            tStage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Table.css").toExternalForm());
            tStage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Main.css").toExternalForm());
            tStage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Message.css").toExternalForm());
            tStage.show();
        }
        catch (IOException io) {
            io.printStackTrace();
            System.out.println("IO-Exception " + io.fillInStackTrace());
        }

    }

    @FXML
    public static void switchScene(String url, String title)  {
        try {
            AnchorPane root = FXMLLoader.load(StageManager.class.getResource(url));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Main.css").toExternalForm());
            stage.getScene().getStylesheets().add(StageManager.class.getResource("../css/TreeView.css").toExternalForm());
            stage.getScene().getStylesheets().add(StageManager.class.getResource("../css/Chart.css").toExternalForm());
            stage.getScene().getStylesheets().add(StageManager.class.getResource("../css/TableView.css").toExternalForm());
            stage.show();
            stage.setOnCloseRequest(event1 -> stage.close());
        }
        catch (IOException io) {
            io.printStackTrace();
            System.out.println("IO-Exception " + io.fillInStackTrace());
        }
    }

    @FXML
    public static void singOut(Event event) {
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent login = FXMLLoader.load(StageManager.class.getResource("../fxml/Login.fxml"));
            Stage stage = new Stage();
            Scene gLScene = new Scene(login);
            gLScene.getStylesheets().add(StageManager.class.getResource("../css/Login.css").toExternalForm());
            stage.setScene(gLScene);
            stage.show();
        }
        catch (IOException ex) {
            System.out.println( "O-Exception: " + ex.getMessage());
        }
        catch (NullPointerException io) {
            System.out.println( "Null-Pointer-Exception: " + io.getMessage());
        }
    }

    private static void sendID(String userID, String userType) {
        if (userType.equals("Teacher")) {
            TeacherController.userID(userID, userType);
        }
        else if (userID.equals("Admin")) {

        }else {
            StudentController.userID(userID, userType);
        }
    }
}
