package com.darsan.game.tools;


import java.sql.*;
public class ConnectDatabase {

    Statement stmt = null;
    Connection conn=null;
    String url;
    public  ConnectDatabase() {

        try {
            // db parameters
             url = "jdbc:sqlite:D:\\mystuff\\Mygame\\1.6\\1.6\\the Mage\\android\\assets\\JTP.sql";
            // create a connection to the database

            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {

        }

    }

   public  void createTable(){

        try {
            if(conn==null)
            conn = DriverManager.getConnection(url);
            System.out.println("Database Opened...\n");
            stmt = conn.createStatement();
            String sql = String.format("CREATE TABLE IF NOT EXISTS Hero(s_id INTEGER PRIMARY KEY, health TEXT NOT NULL,  x TEXT NOT NULL,  y TEXT NOT NULL) ");
            stmt.executeUpdate(sql);
            if(conn==null) {

            }
        }
        catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table Product Created Successfully!!!");
    }
    public  void insert(int id, float x,float y,int health){
        try {
            if(conn==null)
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = String.format("insert into Hero (s_id,health,x,y)values("+id+",'"+health+"','"+x+"','"+y+"')");
            stmt.executeUpdate(sql);
            stmt.close();
            if(conn==null) {

            }else {
                System.out.println("conn not null");
            }
            //darsan.311175008@uhd.edu.iq
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("inserted");

    }
    public ResultSet selectbyId(int id){
        ResultSet resultSet=null;
        try {
            if(conn==null)
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = String.format("select * from Hero where s_id="+id);
            resultSet= stmt.executeQuery(sql);
            if(conn==null) {
                conn.close();
            }else {
                System.out.println("conn not  null");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        System.out.println("selected by id");
        try {
            if(resultSet.next()) {
                System.out.println(resultSet.getString(1)+"  idddd  "+resultSet.getString(2)+"  x"+resultSet.getString(3)+" y "+resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public void update(int id,int health,float x,float y){
        try {
            if(conn==null)
            conn = DriverManager.getConnection(url);
            stmt = conn.createStatement();
            String sql = String.format("UPDATE Hero SET health = '"+health+"', x = '"+x+"',y='"+y+"' WHERE s_id="+id);
           stmt.executeUpdate(sql);
            stmt.close();
            if(conn==null) {

            }else {
                System.out.println("connection not null");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("updated");
    }
    }


