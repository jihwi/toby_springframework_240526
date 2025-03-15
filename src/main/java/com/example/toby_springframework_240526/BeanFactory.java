package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.dao.UserDaoJdbc;
import com.example.toby_springframework_240526.service.DummyMailSender;
import com.example.toby_springframework_240526.service.UserService;
import com.example.toby_springframework_240526.service.UserServiceImpl;
import com.example.toby_springframework_240526.service.aop.*;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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
public class BeanFactory {

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

    @Bean
    public MessageFactoryBean message() {
        String text = "Factory Bean";
        MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
        messageFactoryBean.setText(text);
        return messageFactoryBean;
    }

//    @Bean
//    public TxProxyFactoryBean userTxService(){
//        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
//        txProxyFactoryBean.setTransactionManager(this.platformTransactionManager());
//        txProxyFactoryBean.setTarget(this.userService());
//        txProxyFactoryBean.setServiceInterface(UserService.class);
//        txProxyFactoryBean.setPattern("upgradeLevels");
//        return  txProxyFactoryBean;
//    }
//
    @Bean
    public TransactionAdvice transactionAdvice(){
        TransactionAdvice transactionAdvice = new TransactionAdvice(platformTransactionManager());
        return transactionAdvice;
    }
//
//    @Bean
//    public NameMatchMethodPointcut nameMatchMethodPointcut(){
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("upgrade*");
//        return pointcut;
//    }
//
    @Bean
    public DefaultPointcutAdvisor advisor() {
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(nameMatchClassMethodPointcut(), transactionAdvice());
        return defaultPointcutAdvisor;
    }
//
//    @Bean
//    public ProxyFactoryBean userServiceProxy() {
//        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//        proxyFactoryBean.setTarget(userService());
//
//        proxyFactoryBean.addAdvisor(advisor());
//        return proxyFactoryBean;
//    }

    /**
     * 자동 프록시 생성기
     * @return
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }

    @Bean
    public NameMatchClassMethodPointcut nameMatchClassMethodPointcut() {
        NameMatchClassMethodPointcut nameMatchClassMethodPointcut = new NameMatchClassMethodPointcut();
        nameMatchClassMethodPointcut.setMappedClassName("*ServiceImpl");
        nameMatchClassMethodPointcut.setMappedName("upgrade*");
        return nameMatchClassMethodPointcut;
    }

    @Bean
    public TestUserServiceImpl testUserService() {
        return new TestUserServiceImpl(userDao());
    }
}
