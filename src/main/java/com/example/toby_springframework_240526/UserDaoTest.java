package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {

    private UserDao dao;

    /**
     * 테스트마다 실행전에 사전작업은 before에 중복제거
     * @throws SQLException
     */
    @Before
    public void init() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        dao = context.getBean(UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));
    }

    @Test
    public void addAndGet() throws SQLException {
        User user = new User();
        user.setId("gyumee");
        user.setName("박성철");
        user.setPassword("springno1");

        dao.add(user);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());
        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.get("unknown_id");
    }
}
