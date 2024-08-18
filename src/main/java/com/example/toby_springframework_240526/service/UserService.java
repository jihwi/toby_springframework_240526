package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.domain.User;

public interface UserService {
    void upgradeLevels();
    void add(User user);

}
