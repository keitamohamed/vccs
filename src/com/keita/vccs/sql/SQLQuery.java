package com.keita.vccs.sql;

public class SQLQuery {


    public String getLogin() {
        return "SELECT * FROM User " +
                "WHERE Username = ? AND UPassword = ? " +
                "AND UserType = ?";
    }

    public String getTeachStudent() {
        return "SELECT * FROM User u " +
                "JOIN \nStudent s ON s.SEMPID = u.UserID " +
                "JOIN Class c \nON c.ClassID = s.SClassID " +
                "WHERE s.STEMPID = ?";
    }

    public String getUsers() {
        return "SELECT * FROM User";
    }

    public String getUser() {
        return "SELECT * FROM User u " +
                "WHERE u.UserID = ?";
    }

    public String getStudentTeach() {
        return "SELECT UserID, Name, Email, Phone " +
                "FROM User u " +
                "JOIN \nTeacher t ON t.TEMPID = u.UserID " +
                "JOIN \nStudent s ON s.STEMPID = t.TEMPID " +
                "JOIN \nClass c ON c.ClassID = s.SClassID " +
                "WHERE s.SEMPID = ?\n" +
                "GROUP BY t.TEMPID";
    }

    public String getStudent() {
        return "SELECT UserID, ClassID, Name, Email, Phone " +
                "FROM User u " +
                "JOIN \nStudent s ON s.SEMPID = u.UserID " +
                "JOIN \nClass c ON c.ClassID = s.SClassID " +
                "WHERE s.SEMPID = ?\n" +
                "GROUP BY c.ClassID";
    }

    public String getTeacher() {
        return "SELECT * FROM User " +
                "WHERE UserID = ?";
    }

    public String getClasses() {
        return "SELECT * FROM Class";
    }

    public String getTClass() {
        return "SELECT * FROM Teacher t " +
                "JOIN Class c ON c.ClassID = t.TClassID " +
                "WHERE t.TEMPID = ?";
    }

    public String getSClass() {
        return "SELECT * FROM Student s " +
                "JOIN Class c ON c.ClassID = s.SClassID " +
                "WHERE s.SEMPID = ?";
    }

    public String getClassRegister() {
        return "SELECT DISTINCT ClassID, CTEMPID, Class_Name " +
                "FROM Class c " +
                "JOIN Teacher t ON t.TClassID = c.ClassID";
    }

    public String getGrade() {
        return "SELECT GSEMPID, GClassID, Grade_Name, Score " +
                    "FROM Teacher t " +
                    "JOIN Class c ON c.ClassID = t.TClassID " +
                    "JOIN Student s ON s.SClassID = c.ClassID " +
                    "JOIN Grade g ON g.GSEMPID = s.SEMPID " +
                    "WHERE t.TEMPID = ? " +
                    "GROUP BY g.GradeID";
    }

    public String getSGrade() {
        return "SELECT g.GSEMPID, g.GClassID, Grade_Name, Score " +
                "FROM \nStudent s " +
                "JOIN Class c ON c.ClassID = s.SClassID " +
                "JOIN \nGrade g ON g.GSEMPID = s.SEMPID " +
                "WHERE \ns.SEMPID = ? " +
                "GROUP BY g.GradeID";
    }

    public String getRecord() {
        return "SELECT * FROM Record " +
                "WHERE SEMPID = ? ";
    }

    public String getUpdateScore() {
        return "UPDATE Grade SET Score = ?, Update_Reason = ? " +
                    "WHERE GSEMPID = ? AND GClassID = ? AND Grade_Name = ?";
    }

    public String getAddGrade() {
        return "INSERT INTO Grade (GSEMPID, GClassID, Grade_Name, " +
                    "Score, Update_Reason)" +
                    "VALUE (?, ?, ?, ?, ?)";
    }

    public String getAddRecord() {
        return "INSERT INTO Record (SEMPID, Class_ID, Student_Name, Class_Name, " +
                "Unite, Grade, Term, Year)" +
                    "VALUE (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public String setRegister() {
        return "INSERT INTO Student (SEMPID, STEMPID, SClassID)" +
                "VALUE(?, ?, ?)";
    }

    public String isRecordPresent() {
        return "SELECT * FROM Record " +
                "WHERE SEMPID = ? AND Class_ID = ?";
    }
}
