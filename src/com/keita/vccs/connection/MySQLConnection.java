package com.keita.vccs.connection;

import com.keita.vccs.message.Message;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

public class MySQLConnection implements ConnectionType {

    private String sName;
    private String sPassword;
    private String dbName;
    private String driverName;
    private String url;

    private Message message = new Message();

    @Override
    public Connection mysql() throws ClassNotFoundException, IOException {
        Connection connection = null;

        getPropValues();

        try {

            Class.forName(driverName).getDeclaredConstructor().newInstance();
            Enumeration enumer = DriverManager.getDrivers();
            while (enumer.hasMoreElements()) {
                enumer.nextElement();
            }
        }
        catch (IllegalAccessException e) {
            String errorM = "IllegalAccessException.\n" + e.getStackTrace();
            message.alert("Illegal Access Exception", errorM);
        } catch (InstantiationException e) {
            String errorM = "InstantiationException.\n" + e.getStackTrace();
            message.alert("Instantiation Exception", errorM);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection((url + dbName), sName, sPassword);
        }
        catch (SQLException | NullPointerException sql) {
            String errorM = "Not able to connect to the database. " +
                    "Please make server username and password are correct. " + sql.getMessage();
            message.alert("Error Connecting", errorM);
        }
        return connection;
    }

    public String getPropValues() throws IOException {

        InputStream inputStream = null;

        try {
            Properties prop = new Properties();
            String propFileName = "config/config.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);

            sName = prop.getProperty("serverName");
            sPassword = prop.getProperty("serverPassword");
            dbName = prop.getProperty("dbName");
            driverName = prop.getProperty("driverName");
            url = prop.getProperty("url");

        } catch (Exception e) {
            message.alert("IO-Exception", e.getMessage());
        } finally {
            assert inputStream != null;
            inputStream.close();
        }
        return dbName;
    }

    public String getDbName() {
        try {
            return getPropValues();
        }
        catch (IOException e) {
        }
        return null;
    }
}
