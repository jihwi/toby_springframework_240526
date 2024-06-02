package com.example.toby_springframework_240526;

import java.io.*;

public class Calculator {

    private BufferedReaderContext context;

    public Calculator(BufferedReaderContext context) {
        this.context = context;
    }

    public Integer calcSum(String filePath) {
       return context.lineReadTemplate(new LineCallback() {
           @Override
           public int doSomething(String line, int start) {
               return start + Integer.parseInt(line);
           }
       }, filePath, 0);
    }

    public Integer calcMultiply(String filePath) {
        return context.lineReadTemplate(new LineCallback() {

            @Override
            public int doSomething(String line, int start) {
                return start * Integer.parseInt(line);
            }
        }, filePath, 1);
    }
}
