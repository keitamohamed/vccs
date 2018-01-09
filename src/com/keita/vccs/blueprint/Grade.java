package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Grade {
    private SimpleStringProperty scoreName;
    private SimpleIntegerProperty score;
    private SimpleStringProperty sempID;

    public Grade(String emp, String scoreName, int score) {
        this.scoreName = new SimpleStringProperty(scoreName);
        this.score = new SimpleIntegerProperty(score);
        this.sempID = new SimpleStringProperty(emp);
    }

    @Override
    public String toString() {
        return "Student emp: " + sempID + ", name of score: " + scoreName +
                ", score: " + score;
    }

    public String getScoreName() {
        return scoreName.get();
    }

    public void setScoreName(String scoreName) {
        this.scoreName.set(scoreName);
    }

    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getSempID() {
        return sempID.get();
    }


    public void setSempID(String sempID) {
        this.sempID.set(sempID);
    }
}
