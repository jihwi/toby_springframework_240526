package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * D사 db 연결
 */
public class DSimpleConnectionMaker extends SimpleConnectionMaker{
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        //D사 방법
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring","spring", "book");
        return c;
    }
}
