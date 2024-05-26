package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 커넥션 관심사 클래스로 분리
 * 이로 인해, 다른 도메인 dao별로 db커넥션용 클래스를 만들필요 없이, simpleConnectionMaker을 공통으로 사용하면된다.
 */
public abstract class SimpleConnectionMaker {
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}
