package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import com.example.toby_springframework_240526.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.toby_springframework_240526.service.UserService.MIN_LOGINCOUNT_FOR_SILVER;
import static com.example.toby_springframework_240526.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao dao;

    @Autowired
    private DataSource dataSource;

    private List<User> users;

    @Before
    public void init() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGINCOUNT_FOR_SILVER -1, 0, new Date()),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGINCOUNT_FOR_SILVER, 0, new Date()),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1, new Date()),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, new Date()),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100, new Date())
        );
    }

    @Test
    public void upgradeLevels() throws Exception {
        dao.deleteAll();
        for (User user: users) dao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level level) {
        User userUpdate = dao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(level));
    }

    @Test
    public void add() {
        dao.deleteAll();
        User withLevel = users.get(4);
        User withoutLevel = users.get(0);
        withoutLevel.setLevel(null);

        userService.add(withLevel);
        userService.add(withoutLevel);

        User userWithLevel = dao.get(withLevel.getId());
        User userWithoutLevel = dao.get(withoutLevel.getId());

        assertThat(userWithLevel.getLevel(), is(withLevel.getLevel()));
        assertThat(userWithoutLevel.getLevel(), is(Level.BASIC));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId(), dao, dataSource);
        dao.deleteAll();
        for (User user : users) {
            dao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {}

        checkLevel(users.get(1), Level.BASIC);
    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id, UserDao userDao, DataSource dataSource){
            super(userDao, dataSource);
            this.id = id;
        }

        protected void upgradeLevel(User user){
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}
