package com.example.toby_springframework_240526.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 객체를 생성하는 역할 담당 오브젝트 팩토리 (제어의 역전)
 */
@Configuration
public class DaoFactory {

    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public SimpleConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(dSimpleConnectionMaker());
    }

    @Bean
    public SimpleConnectionMaker dSimpleConnectionMaker() {
        return new DSimpleConnectionMaker();
    }


//    public static AccountDao getAccountDao() {
//        return new AccountDao(new NSimpleConnectionMaker()); //이런식으로 객체마다 생성조건을 다르게 줄수 있음.
//    }
}
