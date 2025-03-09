package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.service.aop.*;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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
@ContextConfiguration(classes = {BeanFactory.class})
public class DynamicProxyTest {

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
        assertThat(((Message) message).getText(), is("Factory Bean"));

        Object bean = context.getBean("&message"); //팩토리 빈이 만들어주는 빈 오브젝트가 아니라 픽토리 빈 자체를 가져오고 싶을경우
        assertThat(bean.getClass(), is(MessageFactoryBean.class));
    }


    @Test
    public void simpleProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Hello.class}, new UpperCaseHandler(new HelloTarget()));
    }

    /**
     * 스프링 제공하는 ProxyFactoryBean
     */
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); //타겟 설정
        pfBean.addAdvice(new UpperCaseAdvice()); //부가기능을 담은 어드바이스를 추가한다. 여러개를 추가할 수 있다.

        Hello proxiedHello = (Hello) pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
    }

    private static class UpperCaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed(); // 리플렉션 method와 달리 메소드 실행시 타겟 오브젝트를 전달할 필요없다. methodInvocation은 메소드 정보와 함꼐 타깃 오브젝트를 알고 있기 때문
            return ret.toUpperCase();
        }
    }

    @Test
    public void pointCutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor(pointcut, new UpperCaseAdvice());
        pfBean.addAdvisor(defaultPointcutAdvisor);

        Hello hello = (Hello) pfBean.getObject();
        assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(hello.sayHi("Toby"), is("HI TOBY"));
        assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
    }
}
