package com.example.toby_springframework_240526.service.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 다이나믹 프록시 인터페이스
 * 어떤 인터페이스에도 부가기능을 이 객체 하나로 적용가능하며,
 * 여러 메소드가 있어도 invoke만 만들어주면 일일히 구현해주지 않아도 됨.
 */
public class UpperCaseHandler implements InvocationHandler {

    private Object target; //어떤 종료의 인터페이스를 구현한 타깃에도 적용가능하도록 object 타입

    public UpperCaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);//타겟 위임
        if (ret instanceof String && method.getName().startsWith("say")) {
            return ((String)ret).toUpperCase(); //부가기능
        } else {
            return ret;
        }
    }
}
