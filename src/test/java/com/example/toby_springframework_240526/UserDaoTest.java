package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class) //테스트중에 사용할 애플리케이션 컨텍스트를 생성
@ContextConfiguration(classes = DaoFactory.class) //설정 정보 세팅
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao dao;

    /**
     * 테스트마다 실행전에 사전작업은 before에 중복제거
     * @throws SQLException
     */
    @Before
    public void init() throws SQLException {
        System.out.println(this.context);
        System.out.println(this);

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
