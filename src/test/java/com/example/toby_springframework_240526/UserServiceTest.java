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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao dao;

    private List<User> users;

    @Before
    public void init() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
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
}
