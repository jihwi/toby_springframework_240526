package com.example.toby_springframework_240526.dao;

import com.example.toby_springframework_240526.service.DummyMailSender;
import com.example.toby_springframework_240526.service.UserService;
import com.example.toby_springframework_240526.service.UserServiceImpl;
import com.example.toby_springframework_240526.service.aop.UserServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 객체를 생성하는 역할 담당 오브젝트 팩토리 (제어의 역전)
 */
@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/spring");
        dataSource.setUsername("spring");
        dataSource.setPassword("book");
        return dataSource;
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(userDao());
        userServiceImpl.setMailSender(mailSender());
        return new UserServiceTx(userServiceImpl, platformTransactionManager());
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource()); //jta db로 변경을 원할시 이부분만 JTATransactionManager 로 생성해주면된다. 하이버네이트라면 HibernateTransactionManager
    }

    @Bean
    public MailSender mailSender() {
        MailSender mailSender = new DummyMailSender();
//        mailSender.setHost("mail.server.com");
        return mailSender;
    }
}
