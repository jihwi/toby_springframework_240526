package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoAdd implements StatementStrategy{


    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement(
                "insert into users(id, name, password) values (?,?,?)"
        );
    }
}
