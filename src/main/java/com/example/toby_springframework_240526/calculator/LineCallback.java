package com.example.toby_springframework_240526.calculator;

public interface LineCallback<T> {

    T doSomething(String line, T start);
}
