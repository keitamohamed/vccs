package com.keita.vccs.sql;

import com.keita.vccs.associate.OtherClasses;
import com.keita.vccs.blueprint.*;
import com.keita.vccs.blueprint.Class;
import com.keita.vccs.connection.MySQLConnection;
import com.keita.vccs.message.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class SQLStatement {
    private static MySQLConnection connect = new MySQLConnection();
    private static Message msg = new Message();
    private static SQLQuery query = new SQLQuery();

    private static Connection connection;
    private static PreparedStatement pStatement;
    private static Statement statement;


    public String login(String username, String password, ChoiceBox<String> userType) {

        connection = null;
        pStatement = null;
        String id = null;

        try {
            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.getLogin());

            pStatement.setString(1, username);
            pStatement.setString(2, password);
            pStatement.setString(3, userType.getValue().trim());
            ResultSet rs = pStatement.executeQuery();

            if (rs.first()) {
                id = Integer.toString(rs.getInt("UserID"));
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            String message = "Username: " + username + ", password " + password +
                    " user type " + userType.getValue() + " is in correct." +
                    " " + ex.getMessage();
            msg.notification("Login Error/No-Match", message);

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                String message = "An SQL-Exception: " + sql.getMessage();
                Message.errorRequire("SQL-Exception", message);
            }
        }
        return id;
    }

    public ObservableList<OtherClasses> loadDate(ObservableList<Teacher> teachers, ObservableList<Class>
            classes, ObservableList<Student> students, String teacherID, String type) {
        ObservableList<OtherClasses> otherClasses = FXCollections.observableArrayList();
        teachers.clear();
        classes.clear();
        students.clear();

        connection = null;
        pStatement = null;
        int idNumber = 0;

        if (teacherID != null) {
            idNumber = Integer.parseInt(teacherID);
        }

        try {
            connection = connect.mysql();
            switch (type) {
                case "Teacher":
                    pStatement = connection.prepareStatement(query.getTeacher());
                    break;
                case "Admin":

                    break;
                default:
                    pStatement = connection.prepareStatement(query.getStudentTeach());
                    break;
            }

            pStatement.setInt(1, idNumber);
            ResultSet rs = pStatement.executeQuery();

            if (rs.first()) {
                String emp = rs.getString("UserID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phoneNum = rs.getString("Phone");
                Teacher teacher = new Teacher(emp, name, email, phoneNum);
                teachers.add(teacher);
            }

            switch (type) {
                case "Teacher":
                    pStatement = connection.prepareStatement(query.getTClass());
                    break;
                case "Admin":

                    break;
                default:
                    pStatement = connection.prepareStatement(query.getSClass());
                    break;
            }

            pStatement.setInt(1, idNumber);
            rs = pStatement.executeQuery();

            while (rs.next()) {
                String emp = rs.getString("ClassID");
                String cName = rs.getString("Class_Name");
                String desc = rs.getString("Description");
                for (int i = 0; i < teachers.size(); i++) {
                    if (teachers.get(i).getClasses().size() == 0) {
                        Class cla = new Class(emp, cName, desc);
                        teachers.get(i).getClasses().add(cla);
                        classes.add(cla);
                    } else {
                        for (int j = 0; j < teachers.size(); j++) {
                            Class c = new Class(emp, cName, desc);
                            teachers.get(i).getClasses().add(c);
                            classes.add(c);
                        }
                    }
                }
            }

            switch (type) {
                case "Teacher":
                    pStatement = connection.prepareStatement(query.getTeachStudent());
                    break;
                case "Admin":

                    break;
                default:
                    pStatement = connection.prepareStatement(query.getStudent());
                    break;
            }

            pStatement.setInt(1, idNumber);
            rs = pStatement.executeQuery();

            while (rs.next()) {
                String emp = rs.getString("UserID");
                String cEmp = rs.getString("ClassID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phoneNum = rs.getString("Phone");

                for (Class aClass : classes) {
                    String id = aClass.getClassID();
                    if (id.equals(cEmp)) {
                        Student student = new Student(emp, cEmp, name, email, phoneNum);
                        students.add(student);
                        aClass.getStudent().add(student);
                    }
                }
            }

            switch (type) {
                case "Teacher":
                    pStatement = connection.prepareStatement(query.getGrade());
                    break;
                case "Admin":

                    break;
                default:
                    pStatement = connection.prepareStatement(query.getSGrade());
                    break;
            }

            pStatement.setInt(1, idNumber);
            rs = pStatement.executeQuery();

            while (rs.next()) {
                String studID = rs.getString("g.GSEMPID");
                String classID = rs.getString("g.GClassID");
                String name = rs.getString("Grade_Name");
                int score = rs.getInt("Score");
                Class cla = getClass(classes, classID);
                if (cla != null) {
                    Student stud = getStudent(cla, studID);
                    if (stud != null) {
                        addStudentGrade(stud, studID, name, score);
                    }
                }
            }

            if (type.equals("Student")) {
                pStatement = connection.prepareStatement(query.getClassRegister());

                rs = pStatement.executeQuery();

                while (rs.next()) {
                    boolean found = false;
                    String emp = rs.getString("CTEMPID");
                    String classID = rs.getString("ClassID");
                    String cName = rs.getString("Class_Name");

                    for (Class cal : classes) {
                        if (cal.getClassID().equals(classID)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        otherClasses.add(new OtherClasses(emp, classID, cName));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException | IOException not) {
            msg.notification("SQL Or IO Exception", not.getMessage());
            not.fillInStackTrace();

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }
        return otherClasses;
    }


    public static void loadRecord(ObservableList<Record> records, String id) {
        connection = null;
        pStatement = null;


        try {

            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.getRecord());

            pStatement.setString(1, id);

            ResultSet rs = pStatement.executeQuery();

            while (rs.next()) {
                String emp = rs.getString("SEMPID");
                String classID = rs.getString("Class_ID");
                String studentName = rs.getString("Student_Name");
                String className = rs.getString("Class_Name");
                String unite = rs.getString("Unite");
                String grade = rs.getString("Grade");
                String term = rs.getString("Term");
                String year = rs.getString("Year");

                records.add(new Record(emp, classID, studentName, className,
                        unite, grade, term, year));
            }
        }
        catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }
    }


    public static void getStudentInfo(ObservableList<Student> students, String id) {
        connection = null;
        pStatement = null;

        try {

            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.getUser());

            pStatement.setString(1, id);

            ResultSet rs = pStatement.executeQuery();

            while (rs.next()) {
                String emp = rs.getString("UserID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                students.addAll(new Student(emp, name, email, phone));
            }
        }
        catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }

    }


    public static void updateStudentScore(TextField emp, TextField classID, TextField name,
                                   TextField score, TextArea reason) {
        connection = null;
        pStatement = null;

        try {
            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.getUpdateScore());

            pStatement.setInt(1, Integer.parseInt(score.getText().trim()));
            pStatement.setString(2, reason.getText().trim());
            pStatement.setString(3, emp.getText().trim());
            pStatement.setString(4, classID.getText().trim());
            pStatement.setString(5, name.getText().trim());
            pStatement.executeUpdate();


        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }
    }


    public static void addGrade(TextField emp, TextField classID, TextField name,
                         TextField score) {
        connection = null;
        pStatement = null;

        try {
            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.getAddGrade());

            pStatement.setString(1, emp.getText().trim());
            pStatement.setString(2, classID.getText().trim());
            pStatement.setString(3, name.getText().trim());
            pStatement.setInt(4, Integer.parseInt(score.getText().trim()));
            pStatement.setString(5, "NA");

            pStatement.executeUpdate();

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }
    }


    public static void addRecord(String emp, String classId, String sName, String cName, String unite,
                          String grade, String term, String year) {
        connection = null;
        pStatement = null;
        boolean present = false;

        try {

            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.isRecordPresent());

            pStatement.setString(1, emp);
            pStatement.setString(2, classId);

            ResultSet rs = pStatement.executeQuery();

            if (rs.first()) {
                present = true;
            }

            if (!present) {
                connection = connect.mysql();
                pStatement = connection.prepareStatement(query.getAddRecord());

                pStatement.setInt(1, Integer.parseInt(emp));
                pStatement.setString(2, classId);
                pStatement.setString(3, sName);
                pStatement.setString(4, cName);
                pStatement.setInt(5, Integer.parseInt(unite));
                pStatement.setString(6, grade);
                pStatement.setString(7, term);
                pStatement.setString(8, year);
                pStatement.executeUpdate();

                Message.successfult("Successfully Inserted", ("Grade was added successfully " +
                        "for " + sName));
            }else {
                Message.exist("Record Exist",
                        ("Couldn't submit " + sName + " final " + cName + " grade because\n" +
                                "it already exist in the recode. Contact Admin for further assistance."));
            }

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }
    }

    public static void registerForClass(String emp, String techEMP, String classID) {
        connection = null;
        pStatement = null;

        try {
            connection = connect.mysql();
            pStatement = connection.prepareStatement(query.setRegister());

            pStatement.setInt(1, Integer.parseInt(emp));
            pStatement.setInt(2, Integer.parseInt(techEMP));
            pStatement.setString(3, classID);

            pStatement.executeUpdate();

        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Message.errorRequire("IO OR SQL Exception", ex.getMessage());

        } finally {
            try {
                closeConnection();
            } catch (SQLException sql) {
                System.out.println(sql.getMessage());
            }
        }

    }

    private static void closeConnection() throws SQLException {
        if (pStatement != null) {
            pStatement.close();
            connection.close();
        }

        if (statement != null) {
            statement.close();
            connection.close();
        }
    }

    private Class getClass(ObservableList<Class> c, String id) {
        for (Class cal : c) {
            if (cal.getClassID().equals(id)) {
                return cal;
            }
        }
        return null;
    }

    private Student getStudent(Class cla, String id) {
        for (Student stud : cla.getStudent()) {
            if (stud.getId().equals(id)) {
                return stud;
            }
        }
        return null;
    }

    private void addStudentGrade(Student stud, String emp, String name, int score) {
        Grade grade = new Grade(emp, name, score);
        stud.getGrades().add(grade);
    }
}
