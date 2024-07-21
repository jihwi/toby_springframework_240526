package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.dao.UserDaoJdbc;
import com.example.toby_springframework_240526.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class) //테스트중에 사용할 애플리케이션 컨텍스트를 생성
@ContextConfiguration(classes = DaoFactory.class) //설정 정보 세팅
public class UserDaoJdbcTest {

    @Autowired
    private ApplicationContext context;

    private UserDao dao;

    private User user1;
    private User user2;

    /**
     * 테스트마다 실행전에 사전작업은 before에 중복제거
     * @throws SQLException
     */
    @Before
    public void init() throws SQLException {
        System.out.println(this.context);
        System.out.println(this);

        dao = context.getBean(UserDaoJdbc.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        user1 = new User();
        user1.setId("gyumee");
        user1.setName("박성철");
        user1.setPassword("springno1");

        user2 = new User();
        user2.setId("whiteship");
        user2.setName("백기선");
        user2.setPassword("married");
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user1.getId());
        assertThat(user2.getName(), is(user1.getName()));
        assertThat(user2.getPassword(), is(user1.getPassword()));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException {
        dao.get("unknown_id");
    }

    @Test
    public void getAll() throws SQLException {
        dao.add(user1);
        dao.add(user2);

        List<User> all = dao.getAll();
        assertThat(all.size(), is(2));

    }

    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user1);
    }
}
