package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Record;
import com.keita.vccs.sqlstatement.SQLStatement;
import com.keita.vccs.util.Utility;
import com.keita.vccs.calculate.Calculation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class GPAController {

    @FXML private Label pEmpT, pNameT, pEmailT, pPhoneT;
    @FXML private LineChart lineChart;

    private ObservableList<Record> records = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Utility.loadStudentInfo();
        SQLStatement.loadRecord(records, StudentController.userID);

        loadRecord();
    }

    private void loadRecord() {
        Utility.studentInfo(pEmpT, pNameT, pEmailT, pPhoneT);
        loadGPA();
    }

    private void loadGPA() {
        ObservableList<String> gap = Calculation.gapByYear(records);
        XYChart.Series<String, Double> date = new XYChart.Series<>();
        for (String str : gap) {
            String[] split = str.split(":");
            date.getData().add(new XYChart.Data<>(split[0], Double.parseDouble(split[1])));
        }
        lineChart.getData().add(date);
    }
}
