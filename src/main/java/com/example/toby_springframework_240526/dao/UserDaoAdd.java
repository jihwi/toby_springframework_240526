package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoAdd implements StatementStrategy{
    private User user;

    public UserDaoAdd(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values (?,?,?)"
        );

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());
        return ps;
    }
}
