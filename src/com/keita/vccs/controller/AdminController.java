package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;

import javax.swing.text.TabableView;

public class AdminController {

    @FXML private Button aHome, aNewUser;
    @FXML private Pane aHomePane, aNewUserPane;
    @FXML private TabableView aUserTable;
    @FXML private TableColumn<User, String> userID, name, email, phone, type;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Class> classes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        dashboard();
    }

    @FXML
    private void dashboard() {
        aHomePane.setVisible(true);
        aNewUserPane.setVisible(false);
    }

    @FXML
    private void newUser() {
        aNewUserPane.setVisible(true);
        aHomePane.setVisible(false);
    }
}
