package com.example.toby_springframework_240526.service.aop;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.User;
import com.example.toby_springframework_240526.service.UserServiceImpl;

public class TestUserServiceImpl extends UserServiceImpl {
    private String id = "madnite1";


    public TestUserServiceImpl(UserDao userDao) {
        super(userDao);
    }

    public void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) throw new RuntimeException();

        super.upgradeLevel(user);
    }
}
