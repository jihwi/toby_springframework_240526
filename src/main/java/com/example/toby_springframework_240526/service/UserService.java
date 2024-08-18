package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
public class UserService {
    public final static int MIN_LOGINCOUNT_FOR_SILVER = 50;
    public final static int MIN_RECCOMEND_FOR_GOLD = 30;

    private final UserDao userDao;
    private final DataSource dataSource;

    public void upgradeLevels() throws Exception {
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); //jta db로 변경을 원할시 이부분만 JTATransactionManager 로 생성해주면된다. 하이버네이트라면 HibernateTransactionManager
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> all = userDao.getAll();

            for (User user : all) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return user.getLogin() >= MIN_LOGINCOUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown level:" + currentLevel);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

}
