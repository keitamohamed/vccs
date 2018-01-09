package com.keita.vccs.controller;

import com.keita.vccs.blueprint.Record;
import com.keita.vccs.message.Message;
import com.keita.vccs.sqlstatement.SQLStatement;
import com.keita.vccs.util.Utility;
import com.keita.vccs.workstation.Method;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FinalGrade {

    private Message message = new Message();

    @FXML private TreeTableView<Record> table;
    @FXML private TreeTableColumn<Record, String> empColumn, cIDColumn, nameColumn;
    @FXML private TreeTableColumn<Record, String> cNameColumn;
    @FXML private TextField empColumnT, classColumnT, nameColumnT;
    @FXML private TextField  tTerm, cNameColumnT, gradeColumnT, number, unitesT;
    @FXML private ChoiceBox<String> fClassType;
    @FXML private DatePicker datePicker;
    @FXML private Button fSubmit;

    private ObservableList<String> classList = FXCollections.observableArrayList();

    private static TreeItem<Record> record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));

    @FXML
    public void initialize() {
        if (classList.size() == 0) {
            record.getChildren().clear();
            record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));
            Utility.finalGradeTable(table, record, empColumn, cIDColumn, nameColumn,
                    cNameColumn, fClassType, classList);
        }

        fClassType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            record.getChildren().clear();
            clearTextField();
            record = new TreeItem<>(new Record("EMP ID", "CLASS ID", "STUDENT NAME", "CLASS NAME"));
            Utility.finalGradeTable(table, record, empColumn, cIDColumn, nameColumn,
                    cNameColumn, fClassType, classList);
        });
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> Utility.finalGradeText(newValue, empColumnT, classColumnT, nameColumnT, cNameColumnT, gradeColumnT));
        number.textProperty().addListener((observable, oldValue, newValue) ->
                Utility.calculateGrade(empColumnT.getText(), classColumnT.getText(), newValue, gradeColumnT));

        fSubmit.setOnAction(e -> {
            if (isFilledOut() && datePicker.getValue() != null) {
                SQLStatement.addRecord(empColumnT.getText(), classColumnT.getText(), nameColumnT.getText(), cNameColumnT.getText(),
                        unitesT.getText(), gradeColumnT.getText(), tTerm.getText(),
                        Integer.toString(datePicker.getValue().getYear()));
                message.alert("Successfully Inserted", ("Grade was added successfully " +
                        "for " + nameColumnT.getText()));
            } else {
                message.alert("Field's Require", ("All filed's are require. " +
                        "Please make sure all field have a value."));
            }
        });
    }

    @FXML
    private void clearTextField() {
        empColumnT.clear();
        classColumnT.clear();
        nameColumnT.clear();
        cNameColumnT.clear();
    }

    @FXML
    private boolean isFilledOut () {
        return !empColumnT.getText().equals("") && !classColumnT.getText().equals("") && !nameColumnT.getText().equals("") &&
                !tTerm.getText().equals("") && !cNameColumnT.getText().equals("") && !gradeColumnT.getText().equals("") &&
                !unitesT.getText().equals("");
    }

}
