package com.keita.vccs.blueprint;

import javafx.beans.property.SimpleStringProperty;

public class ScoreTable {
    private SimpleStringProperty id;
    private SimpleStringProperty classID;
    private SimpleStringProperty name;
    private SimpleStringProperty scoreName;
    private SimpleStringProperty score;

    public ScoreTable (){};

    public ScoreTable(String id, String classID, String name, String scoreName, String score) {
        this.id = new SimpleStringProperty(id);
        this.classID = new SimpleStringProperty(classID);
        this.name = new SimpleStringProperty(name);
        this.scoreName = new SimpleStringProperty(scoreName);
        this.score = new SimpleStringProperty(score);
    }

    @Override
    public String toString() {
        return "EMP ID: " + id + " class id " + classID +  " name " + name;
    }

    public String getId() {
        return id.get();
    }

    public String getClassID() {
        return classID.get();
    }

    public String getName() {
        return name.get();
    }


    public String getScoreName() {
        return scoreName.get();
    }

    public String getScore() {
        return score.get();
    }

}
