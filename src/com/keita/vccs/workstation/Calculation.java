package com.keita.vccs.workstation;

import com.keita.vccs.blueprint.Class;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.message.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public abstract class Calculation {
    private static Message msg = new Message();
    private static ObservableList<AddingPoint> addingPoints = FXCollections.observableArrayList();

    public static int addScore(ObservableList<ScoreTable> scoreTables, String emp, String classID, int num) {
        int point = 0, counter = 0;
        if (!classID.isEmpty()) {
            for (ScoreTable score : scoreTables) {
                if (score.getId().equals(emp)) {
                    if (score.getClassID().equals(classID)) {
                        if (Integer.parseInt(score.getScore()) <= 10) {
                            point += (Integer.parseInt(score.getScore()) * 10 );
                            counter++;
                        }else {
                            point += Integer.parseInt(score.getScore());
                            counter++;
                        }
                    }
                }
            }
        }

        if ((num <= counter) && (num > 0)) {
            return (point / num);
        }else {
            String message = "According to you, this student " +
                    "total number of (Test + Assignment + Home-work + Mid-term and Exam) is " +
                    "" + counter + ". Ether enter a number less then " + counter + " or the " +
                    "student grade will be reset back.";
            msg.alert("Invalid Input", message);
        }
        return addScore(scoreTables, emp, classID);
    }

    public static int addScore(ObservableList<Class> classes, String className) {
        int score = 0, counter = 0;
        if (classes.size() > 0) {
            for (Class cal : classes) {
                for (Student stud : cal.getStudent()) {
                    for (Grade grade : stud.getGrades()) {
                        if (cal.getClassName().equals(className)) {
                            if (grade.getScore() <= 10) {
                                score += (grade.getScore() * 10);
                                counter++;
                                continue;
                            }
                            score += grade.getScore();
                            counter++;
                        }
                    }
                }
            }
        }
        if (counter > 0) {
            return (score / counter);
        }
        return -1;
    }

    public static int addScore(ObservableList<ScoreTable> scoreTables, String emp, String classID) {
        int point = 0,  numCount = 0;
        for (ScoreTable score : scoreTables) {
            if (score.getId().equals(emp)) {
                if (score.getClassID().equals(classID)) {
                    int sco = Integer.parseInt(score.getScore());
                    if (sco <= 10) {
                        point += (sco * 10 );
                        numCount++;
                    }else {
                        point += sco;
                        numCount++;
                    }
                }
            }
        }
        if (numCount != 0) {
            return (point / numCount);
        }
        return -1;
    }

    public static ObservableList<String> gapByYear(ObservableList<Record> records) {
        ObservableList<String> gpa = FXCollections.observableArrayList();
        point(records);
        for (AddingPoint add : addingPoints) {
            gpa.add(add.getYear() + " : " + (add.getPointEarn() / add.getTotalUnites()));
        }
        return gpa;
    }

    private static void point(ObservableList<Record> records) {
        double point;
        int unite;
        for (Record rec : records) {
            if (addingPoints.size() == 0) {
                point = classGap(rec.getGrade());
                unite = Integer.parseInt(rec.getUnite());
                addingPoints.add(new AddingPoint(rec.getYear(), (point * unite), unite));
                continue;
            }

            AddingPoint getExisting = index(rec.getYear());

            if (getExisting != null) {
                point = classGap(rec.getGrade());
                unite = Integer.parseInt(rec.getUnite());

                double newPoint = getExisting.getPointEarn() + (point * unite);
                int newUnite = getExisting.getTotalUnites() + unite;
                getExisting.setPointEarn(newPoint);
                getExisting.setTotalUnites(newUnite);
                continue;
            }

            AddingPoint previous = addingPoints.get(addingPoints.size() - 1);

            unite = Integer.parseInt(rec.getUnite());
            point = classGap(rec.getGrade()) * unite;

            addingPoints.add(new AddingPoint(rec.getYear(), (point + previous.getPointEarn()),
                    (unite + previous.getTotalUnites())));
        }
    }

    public static String calculateLetterGrade(int grade) {
        if (grade > 110) {
            msg.alert("Instructor Error", ("You make an error. " +
                    "Student can't have " + grade + " it must be 110 " +
                    "or lower then that." ));
        }
        if ((grade >= 100) && (grade <= 110)) {
            return "A+";
        }
        else if ((grade >= 90) && (grade <= 100)) {
            return "A";
        } else if ((grade >= 80) && (grade < 90)) {
            return "B";
        } else if ((grade >= 70) && (grade < 80)) {
            return "C";
        } else if ((grade >= 60) && (grade < 70)) {
            return "D";
        }
        return "F";
    }

    private static double classGap(String grade) {
        switch (grade) {
            case "A":
            case "A+":
                if (grade.equals("A+")) {
                    return 4.7;
                }
                return 4;
            case "B":
            case "B+":
                if (grade.equals("B+")) {
                    return 3.7;
                }
                return 3;
            case "C":
            case "C+":
                if (grade.equals("C+")) {
                    return 2.7;
                }
                return 2;
            case "D":
            case "D+":
                if (grade.equals("D+")) {
                    return 1.7;
                }
                return 1;
        }
        return 0;
    }

    private static AddingPoint index(String year) {

        AddingPoint add = null;
        for (int i = 0; i < addingPoints.size(); i++) {
            if (addingPoints.get(i).getYear().equals(year)) {
                add = addingPoints.get(i);
            }
        }
        if (add != null) {
            return add;
        }
        return null;
    }
}

class AddingPoint {
    private String year;
    private double pointEarn;
    private int totalUnites;

    public AddingPoint(String year, double point, int unite) {
        this.year = year;
        this.pointEarn = point;
        this.totalUnites = unite;
    }

    public String getYear() {
        return year;
    }

    public double getPointEarn() {
        return pointEarn;
    }

    public int getTotalUnites() {
        return totalUnites;
    }

    public void setPointEarn(double pointEarn) {
        this.pointEarn = pointEarn;
    }

    public void setTotalUnites(int totalUnites) {
        this.totalUnites = totalUnites;
    }
}
