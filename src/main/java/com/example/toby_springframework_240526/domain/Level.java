package com.example.toby_springframework_240526.domain;

public enum Level {

    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next) {
        this.value = value;
        this.next = next;
    }

    public int getValue() {
        return value;
    }

    public Level getNext() {
        return next;
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
