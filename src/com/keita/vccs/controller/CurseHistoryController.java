package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Record;
import com.keita.vccs.blueprint.Student;
import com.keita.vccs.sqlstatement.SQLStatement;
import com.keita.vccs.util.Utility;
import com.keita.vccs.workstation.Method;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CurseHistoryController {

    @FXML private Label cEmpT, cNameT, cEmailT, cNumT;
    @FXML private TableView<Record> table;
    @FXML private TableColumn<Record, String> cTEmp, cTName, cTUnites, cTGrade, cTTerm, cYear;

    private ObservableList<Record> records = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Utility.loadStudentInfo();
        SQLStatement.loadRecord(records, StudentController.userID);
        loadRecord();
    }

    private void loadRecord() {
        Utility.studentInfo(cEmpT, cNameT, cEmailT, cNumT);
        loadTable();
    }

    private void loadTable() {
        cTEmp.setCellValueFactory(new PropertyValueFactory<>("classID"));
        cTName.setCellValueFactory(new PropertyValueFactory<>("className"));
        cTUnites.setCellValueFactory(new PropertyValueFactory<>("unite"));
        cTGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        cTTerm.setCellValueFactory(new PropertyValueFactory<>("term"));
        cYear.setCellValueFactory(new PropertyValueFactory<>("year"));

        if (table.getColumns().size() > 0 || table.getItems().size() > 0) {
            table.getItems().clear();
            table.getColumns().clear();
        }
        table.setItems(records);
        table.getColumns().addAll(cTEmp, cTName, cTUnites, cTGrade, cTTerm, cYear);
    }
}
