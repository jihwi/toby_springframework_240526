package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private JdbcTemplate jdbcTemplate; //스프링에서 제공하는 템플릿/콜백

    private RowMapper<User> rowMapper = new RowMapper<>() {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));

            return user;
        }
    };

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws SQLException {
        jdbcTemplate.update("insert into users values (?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public User get(String id) throws SQLException, EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, this.rowMapper);
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.query("select count(*) from users", new ResultSetExtractor<Integer>() {

            @Override
            public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                resultSet.next();
                return resultSet.getInt(1);
            }
        });
    }

    public List<User> getAll() throws SQLException {
        return jdbcTemplate.query("select * from users order by id", this.rowMapper);
    }
}
