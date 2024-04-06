package com.taskmanagement.task_management_app.db_connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnect {
    private static String HOST = "localhost";
    private static int PORT = 5432;
    private static String DB_NAME = "taskmaster_db";
    private static String USERNAME = "postgres";
    private static String PASSWORD = "1234";
    private static Connection connection ;


    public static Connection getConnect (){
        try {
            connection = DriverManager.getConnection(String.format("jdbc:postgresql://%s:%d/%s", HOST,PORT,DB_NAME),USERNAME,PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }

        return  connection;
    }
}
