package com.example.toby_springframework_240526;

import java.io.*;

public class Calculator {

    private BufferedReaderContext context;

    public Calculator(BufferedReaderContext context) {
        this.context = context;
    }

    public Integer calcSum(String filePath) {
       return context.fileReadTemplate(new BufferedReaderCallback() {
            @Override
            public Integer doSomething(BufferedReader br) throws IOException {
                int sum = 0;
                String line;

                while((line = br.readLine()) != null) {
                    sum+=Integer.parseInt(line);
                }

                return sum;
            }
        }, filePath);
    }

    public Integer calcMultiply(String filePath) {
        return context.fileReadTemplate(new BufferedReaderCallback() {
            @Override
            public Integer doSomething(BufferedReader br) throws IOException {
                int multiply = 1;
                String line;

                while((line = br.readLine()) != null) {
                    multiply *= Integer.parseInt(line);
                }

                return multiply;
            }
        }, filePath);
    }
}
