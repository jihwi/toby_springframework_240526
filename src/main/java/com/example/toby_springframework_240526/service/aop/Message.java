package com.example.toby_springframework_240526.service.aop;

public class Message {

    private String text;

    private Message (String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
