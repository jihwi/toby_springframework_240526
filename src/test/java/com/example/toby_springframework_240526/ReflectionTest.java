package com.example.toby_springframework_240526;

import com.example.toby_springframework_240526.service.Hello;
import com.example.toby_springframework_240526.service.HelloTarget;
import com.example.toby_springframework_240526.service.HelloUppercase;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ReflectionTest {

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
}
