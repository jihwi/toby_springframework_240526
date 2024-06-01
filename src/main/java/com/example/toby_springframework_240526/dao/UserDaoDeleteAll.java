package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll implements StatementStrategy{


    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement(
                "delete from users"
        );
    }
}
