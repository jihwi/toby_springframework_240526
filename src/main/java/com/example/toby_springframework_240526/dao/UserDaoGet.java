package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoGet implements StatementStrategy{
    private String id;

    public UserDaoGet(String id) {
        this.id = id;
    }

    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);
        return ps;

    }
}
