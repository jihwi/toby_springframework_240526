package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.domain.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    void deleteAll();
    User get(String id);
    int getCount();
    List<User> getAll();
    void update(User user);
}
