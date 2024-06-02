package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        this.jdbcContextWithStatementStrategy(new StatementStrategy() {
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
        });
    }

    public void deleteAll() throws SQLException {
        this.jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makeStatement(Connection c) throws SQLException {
                return c.prepareStatement(
                        "delete from users"
                );
            }
        });
    }

    private void jdbcContextWithStatementStrategy(StatementStrategy statementStrategy) throws SQLException {
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

    public User get(String id) throws SQLException, EmptyResultDataAccessException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(
                    "select * from users where id = ?"
            );
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            User user = null;
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (user == null) {
                throw new EmptyResultDataAccessException(1);
            }

            return user;

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

    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement(
                    "select count(*) from users"
            );
            ResultSet rs = ps.executeQuery();
            rs.next();

            return rs.getInt(1);
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
}
