package com.example.toby_springframework_240526;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BufferedReaderContext {

    public Integer fileReadTemplate(BufferedReaderCallback callback, String filePath) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filePath));
            return callback.doSomething(br);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
