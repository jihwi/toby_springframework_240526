package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import com.example.toby_springframework_240526.service.MockMailSender;
import com.example.toby_springframework_240526.service.UserService;
import com.example.toby_springframework_240526.service.UserServiceImpl;
import com.example.toby_springframework_240526.service.aop.TestUserServiceImpl;
import com.example.toby_springframework_240526.service.aop.TransactionHandler;
import com.example.toby_springframework_240526.service.aop.TxProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.toby_springframework_240526.service.UserServiceImpl.MIN_LOGINCOUNT_FOR_SILVER;
import static com.example.toby_springframework_240526.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanFactory.class)
public class UserServiceTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao dao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);

        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserDao);
        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(this.users.get(1)); //파라미터 객체 검증
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(this.users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class); //넘긴 파라미터 내부 정보 확인시 유용
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> allValues = mailMessageArg.getAllValues();
        assertThat(allValues.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(allValues.get(1).getTo()[0], is(users.get(3).getEmail()));


//        List<User> updated = mockUserDao.getUpdated();
//        assertThat(updated.size(), is(2));
//        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
//        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
//
//        List<String> requests = mockMailSender.getRequests();
//        assertThat(requests.size(), is(2));
//        assertThat(requests.get(0), is(users.get(1).getEmail()));
//        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User user, String expectedId, Level expectedLevel) {
        assertThat(user.getId(), is(expectedId));
        assertThat(user.getLevel(), is(expectedLevel));
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

    @Autowired
    UserService testUserService;

    @Test
//    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {

//        TestUserService testUserService = new TestUserService(users.get(3).getId(), dao);
//        testUserService.setMailSender(new MockMailSender());
//
////        UserServiceTx userServiceTx = new UserServiceTx(testUserService, platformTransactionManager);
//
//        ProxyFactoryBean bean = context.getBean("&userServiceProxy", ProxyFactoryBean.class);
//        bean.setTarget(testUserService);
//
//        UserService userServiceTx = (UserService) bean.getObject();

        dao.deleteAll();
        for (User user : users) {
            dao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
        }

        checkLevel(users.get(1), Level.BASIC);
    }

//    @Test
//    public void upgradeAllOrNothingByDynamicProxy() {
//        TestUserService testUserService = new TestUserService(users.get(3).getId(), dao);
//        testUserService.setMailSender(new MockMailSender());
//
//        dao.deleteAll();
//        for (User user : users) {
//            dao.add(user);
//        }
//
//        TransactionHandler transactionHandler = new TransactionHandler(testUserService, platformTransactionManager);
//        transactionHandler.setPattern("upgradeLevels");
//
//        UserService proxyInstance = (UserService) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{UserService.class}, transactionHandler);
//
//        try {
//            proxyInstance.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//        }
//        checkLevel(users.get(1), Level.BASIC);
//    }

//    @Test
//    @DirtiesContext //빈 팩토리 설정을 바꾸는 작업이 있어서 (testUserService) 설정. 다이내믹 프록시 팩토리 빈을 직접 맏르어 사용할때는 없앴다가 다시 등장한 컨텍스트 무효화 애노테이션
//    public void upgradeAllOrNothingByDynamicProxyWithFactoryBean() throws Exception {
//        TestUserService testUserService = new TestUserService(users.get(3).getId(), dao);
//        testUserService.setMailSender(new MockMailSender());
//
//        dao.deleteAll();
//        for (User user : users) {
//            dao.add(user);
//        }
//
//        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userTxService", TxProxyFactoryBean.class);
//        txProxyFactoryBean.setTarget(testUserService);
//        UserService userTxService = (UserService) txProxyFactoryBean.getObject();
//
//        try {
//            userTxService.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//        }
//        checkLevel(users.get(1), Level.BASIC);
//    }

//    static class TestUserService extends UserServiceImpl {
//        private String id;
//
//        private TestUserService(String id, UserDao userDao) {
//            super(userDao);
//            this.id = id;
//        }
//
//        protected void upgradeLevel(User user) {
//            if (user.getId().equals(id)) {
//                throw new TestUserServiceException();
//            }
//            super.upgradeLevel(user);
//        }
//    }

    static class TestUserServiceException extends RuntimeException {

    }

    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        public MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }


        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException(); //테스트에 사용되지 않는 메소드
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

}
