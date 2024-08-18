package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import com.example.toby_springframework_240526.service.MockMailSender;
import com.example.toby_springframework_240526.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.toby_springframework_240526.service.UserService.MIN_LOGINCOUNT_FOR_SILVER;
import static com.example.toby_springframework_240526.service.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao dao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private MailSender mailSender;

    private List<User> users;

    @Before
    public void init() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGINCOUNT_FOR_SILVER - 1, 0, new Date(), null),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGINCOUNT_FOR_SILVER, 0, new Date(), "test@test.com"),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1, new Date(), null),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, new Date(), "test2@test.com"),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100, new Date(), null)
        );
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception {
        dao.deleteAll();
        for (User user : users) dao.add(user);

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
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
        TestUserService testUserService = new TestUserService(users.get(3).getId(), dao, platformTransactionManager);
        testUserService.setMailSender(mailSender);
        dao.deleteAll();
        for (User user : users) {
            dao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevel(users.get(1), Level.BASIC);
    }

    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id, UserDao userDao, PlatformTransactionManager platformTransactionManager) {
            super(userDao, platformTransactionManager);
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}
