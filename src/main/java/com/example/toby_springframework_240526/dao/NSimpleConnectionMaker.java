package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * N사 DB 연결
 */
public class NSimpleConnectionMaker extends SimpleConnectionMaker{
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/spring","spring", "book");
        return c;
    }
}
