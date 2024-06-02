package com.example.toby_springframework_240526;

public interface LineCallback<T> {

    T doSomething(String line, T start);
}
