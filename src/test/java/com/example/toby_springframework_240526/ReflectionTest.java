package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.dao.DaoFactory;
import com.example.toby_springframework_240526.service.aop.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoFactory.class})
public class ReflectionTest {

    @Autowired
    ApplicationContext context;

    @Test
    public void invokeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "Spring";
        assertThat(name.length(), is(6));

        Method length = name.getClass().getMethod("length");
        assertThat(length.invoke(name), is(6));

        assertThat((Character) name.charAt(0), is('S'));

        Method charAt = name.getClass().getMethod("charAt", int.class);
        assertThat((Character) charAt.invoke(name, 0), is('S'));
    }

    @Test
    public void helloProxy() {
        Hello hello = new HelloUppercase(new HelloTarget());
        assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class}, new UpperCaseHandler((new HelloTarget())));
        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
    }

    @Test
    public void factoryBean() {
        Object message = context.getBean("message");
        assertThat(message.getClass(), is(Message.class));
        assertThat(((Message)message).getText(), is("Factory Bean"));

        Object bean = context.getBean("&message"); //팩토리 빈이 만들어주는 빈 오브젝트가 아니라 픽토리 빈 자체를 가져오고 싶을경우
        assertThat(bean.getClass(), is(MessageFactoryBean.class));
    }
}
