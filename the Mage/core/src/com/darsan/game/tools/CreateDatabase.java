package com.darsan.game.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDatabase {



    public  void createNewDatabase (String fileName){

        String url = "jdbc:sqlite:D:\\mystuff\\Mygame\\1.6\\1.6\\the Mage\\android\\assets\\" + fileName;

        try {

            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}