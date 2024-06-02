package com.example.toby_springframework_240526;

public class Calculator {

    private BufferedReaderContext context;

    public Calculator(BufferedReaderContext context) {
        this.context = context;
    }

    public Integer calcSum(String filePath) {
       return context.lineReadTemplate(new LineCallback<Integer>() {

           @Override
           public Integer doSomething(String line, Integer start) {
               return start + Integer.parseInt(line);
           }
       }, filePath, 0);
    }

    public Integer calcMultiply(String filePath) {
        return context.lineReadTemplate(new LineCallback<Integer>() {

            @Override
            public Integer doSomething(String line, Integer start) {
                return start * Integer.parseInt(line);
            }
        }, filePath, 1);
    }

    public String concatenate(String filePath) {
        return context.lineReadTemplate(new LineCallback<String>() {
            @Override
            public String doSomething(String line, String start) {
                return start + line;
            }
        }, filePath, "");
    }
}
