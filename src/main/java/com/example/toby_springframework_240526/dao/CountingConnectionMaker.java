package com.example.toby_springframework_240526.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * db커넥션마다 카운팅하는 부가기능
 */
public class CountingConnectionMaker extends SimpleConnectionMaker {

    private SimpleConnectionMaker simpleConnectionMaker;
    private int counter = 0;

    public CountingConnectionMaker(SimpleConnectionMaker simpleConnectionMaker) {
        this.simpleConnectionMaker = simpleConnectionMaker;
    }

    public int getCounter() {return this.counter;}

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        this.counter++;
        return simpleConnectionMaker.getConnection();
    }
}
