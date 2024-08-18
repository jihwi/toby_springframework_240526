package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService {
    private final UserServiceImpl userServiceImpl;
    private final PlatformTransactionManager transactionManager;

    public UserServiceTx(UserServiceImpl userServiceImpl, PlatformTransactionManager transactionManager) {
        this.userServiceImpl = userServiceImpl;
        this.transactionManager = transactionManager;
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userServiceImpl.upgradeLevels();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        userServiceImpl.add(user);
    }

}
