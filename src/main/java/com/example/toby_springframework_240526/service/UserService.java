package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    public final static int MIN_LOGINCOUNT_FOR_SILVER = 50;
    public final static int MIN_RECCOMEND_FOR_GOLD = 30;

    private final UserDao userDao;
    private final DataSource dataSource;

    public UserService(UserDao userDao, DataSource dataSource) {
        this.userDao = userDao;
        this.dataSource = dataSource;
    }

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        Connection c = DataSourceUtils.getConnection(dataSource);
        c.setAutoCommit(false);

        try {
            List<User> all = userDao.getAll();

            for (User user : all) {
                if(canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            c.commit();
        }catch (Exception e) {
            c.rollback();
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(c, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC : return user.getLogin() >= MIN_LOGINCOUNT_FOR_SILVER ;
            case SILVER: return user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown level:" + currentLevel);
        }
    }

    public void add(User user){
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

}
