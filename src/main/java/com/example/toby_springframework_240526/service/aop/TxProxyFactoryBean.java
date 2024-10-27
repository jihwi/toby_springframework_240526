package com.example.toby_springframework_240526.service.aop;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    private Class<?> serviceInterface; //userService 외의 다른 인터페이스 타겟에도 적용할수 있도록

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler(target, transactionManager);
        transactionHandler.setPattern(pattern);
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{serviceInterface}, transactionHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
