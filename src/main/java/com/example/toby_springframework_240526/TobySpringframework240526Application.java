package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.CountingConnectionMaker;
import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.dao.NSimpleConnectionMaker;
import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

@SpringBootApplication
public class TobySpringframework240526Application {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SpringApplication.run(TobySpringframework240526Application.class, args);


        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

        CountingConnectionMaker countingConnectionMaker = context.getBean(CountingConnectionMaker.class);
        System.out.println("connection counter : " + countingConnectionMaker.getCounter());
    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
//        UserDao userDao1 = context.getBean("userDao", UserDao.class);
//        UserDao userDao2 = context.getBean("userDao", UserDao.class);
//
//        System.out.println(userDao1);
//        System.out.println(userDao2); //동일 객체 (싱글톤)
//    }
}
