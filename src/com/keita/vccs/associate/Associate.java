package com.keita.vccs.associate;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Associate {

    @FXML
    public void changeStage(String url, String title)  {

        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane teacherPane = FXMLLoader.load(getClass().getResource(url));
            loader.setRoot(teacherPane);
            Scene scene = new Scene(teacherPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.getScene().getStylesheets().add(getClass().getResource("../css/Main.css").toExternalForm());
            stage.getScene().getStylesheets().add(getClass().getResource("../css/TreeView.css").toExternalForm());
            stage.getScene().getStylesheets().add(getClass().getResource("../css/Chart.css").toExternalForm());
            stage.getScene().getStylesheets().add(getClass().getResource("../css/TableView.css").toExternalForm());
            stage.show();
            stage.setOnCloseRequest(event1 -> stage.close());
        }
        catch (IOException io) {
            io.printStackTrace();
            System.out.println("IO-Exception " + io.fillInStackTrace());
        }
    }

    @FXML
    public void singOut(Event event) {
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Parent vccs = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
            Stage stage = new Stage();
            Scene gLScene = new Scene(vccs);
            gLScene.getStylesheets().add(getClass().getResource("../css/Login.css").toExternalForm());
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
}
