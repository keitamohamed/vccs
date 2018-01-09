package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;

public class StudentT {
    private SimpleStringProperty classID;
    private SimpleStringProperty scoreName;
    private SimpleStringProperty score;

    public StudentT(String classID, String scoreName, String score) {
        this.classID = new SimpleStringProperty(classID);
        this.scoreName = new SimpleStringProperty(scoreName);
        this.score = new SimpleStringProperty(score);
    }

    public String getClassID() {
        return classID.get();
    }


    public String getScoreName() {
        return scoreName.get();
    }


    public String getScore() {
        return score.get();
    }
}
