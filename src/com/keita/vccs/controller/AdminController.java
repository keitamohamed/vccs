package com.keita.vccs.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AdminController {
    @FXML private Button aHome, aNewUser;
    @FXML private Pane aHomePane, aNewUserPane;

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
