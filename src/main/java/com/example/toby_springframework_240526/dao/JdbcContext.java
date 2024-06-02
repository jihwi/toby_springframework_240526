package com.example.toby_springframework_240526.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {

    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
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

    public void executeSql(String sql) throws SQLException {
        this.jdbcContextWithStatementStrategy(new StatementStrategy() {

            @Override
            public PreparedStatement makeStatement(Connection c) throws SQLException {
                return c.prepareStatement(
                        sql
                );
            }
        });
    }

    public void executeSql(String sql, String... params) throws SQLException {
        this.jdbcContextWithStatementStrategy(new StatementStrategy() {

            @Override
            public PreparedStatement makeStatement(Connection c) throws SQLException {
                PreparedStatement ps = c.prepareStatement(sql);
                int i = 1;
                for (String param : params) {
                    ps.setString(i, param);
                    i++;
                }
                return  ps;
            }
        });
    }
}
