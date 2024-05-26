package com.example.toby_springframework_240526.dao;

/**
 * 객체를 생성하는 역할 담당 오브젝트 팩토리 (제어의 역전)
 */
public class DaoFactory {

    public static UserDao getUserDao() {
        return new UserDao(new DSimpleConnectionMaker());
    }


//    public static AccountDao getAccountDao() {
//        return new AccountDao(new NSimpleConnectionMaker()); //이런식으로 객체마다 생성조건을 다르게 줄수 있음.
//    }
}
