package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.dao.UserDaoJdbc;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private User user3;

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

        user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
        user2 = new User("whiteship", "백기선","married", Level.SILVER, 55, 10);
        user3 = new User("bumjin", "박범진", "spring3", Level.GOLD, 100, 40);
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user1.getId());
        checkSameUser(user1, user2);
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

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is (user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getLogin(), is(user2.getLogin()));
        assertThat(user1.getRecommend(), is(user2.getRecommend()));
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("spring6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User userUpdate = dao.get(user1.getId());
        checkSameUser(user1, userUpdate);

        User user2same = dao.get(user2.getId());
        checkSameUser(user2 ,user2same);
    }

}
