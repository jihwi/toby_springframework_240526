package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.User;
import org.junit.runner.JUnitCore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class TobySpringframework240526Application {

    public static void main(String[] args) throws SQLException {
//        SpringApplication.run(TobySpringframework240526Application.class, args);
        JUnitCore.main("com.example.toby_springframework_240526.UserDaoTest");
    }
}
