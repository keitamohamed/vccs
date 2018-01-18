package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Grade {

    private SimpleStringProperty classID;
    private SimpleStringProperty scoreName;
    private SimpleIntegerProperty score;

    public Grade () {}

    public Grade(String emp, String scoreName, int score) {
        this.scoreName = new SimpleStringProperty(scoreName);
        this.score = new SimpleIntegerProperty(score);
        this.classID = new SimpleStringProperty(emp);
    }

    @Override
    public String toString() {
        return "Student emp: " + classID + ", name of score: " + scoreName +
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

    public String getClassID() {
        return classID.get();
    }


    public void setClassID(String classID) {
        this.classID.set(classID);
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }
}
