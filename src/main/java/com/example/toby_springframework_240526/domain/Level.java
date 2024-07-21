package com.example.toby_springframework_240526.domain;

public enum Level {
    BASIC(1),
    SILVER(2),
    GOLD(3);

    private int value;

    Level(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static Level valueOf(int value) {
        for (Level level : Level.values()) {
            if (level.getValue() == value) {
                return level;
            }
        }

        throw new AssertionError("Unknown value : " + value);
    }
}
