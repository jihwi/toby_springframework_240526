package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;

import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> all = userDao.getAll();

        for (User user : all) {
            if(canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC : return user.getLogin() >= 50 ;
            case SILVER: return user.getRecommend() >= 30;
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
