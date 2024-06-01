package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDao {

    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        UserDaoAdd userDaoAdd = new UserDaoAdd(user);
        this.jdbcContextWithStatementStrategyVoid(userDaoAdd);
    }

    public void deleteAll() throws SQLException {
        UserDaoDeleteAll userDaoDeleteAll = new UserDaoDeleteAll();
        this.jdbcContextWithStatementStrategyVoid(userDaoDeleteAll);
    }

    private void jdbcContextWithStatementStrategyVoid(StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = statementStrategy.makeStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private ResultSet jdbcContextWithStatementStrategyResultSet(StatementStrategy statementStrategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = statementStrategy.makeStatement(c);
            return ps.executeQuery();

        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public User get(String id) throws SQLException, EmptyResultDataAccessException {
        UserDaoGet userDaoGet = new UserDaoGet(id);
        ResultSet rs = this.jdbcContextWithStatementStrategyResultSet(userDaoGet);

        User user = null;
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }
        return user;
    }

    public int getCount() throws SQLException {
       UserDaoCount userDaoCount = new UserDaoCount();
        ResultSet rs = this.jdbcContextWithStatementStrategyResultSet(userDaoCount);
        rs.next();

        return rs.getInt(1);
    }
}
