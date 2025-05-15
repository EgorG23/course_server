package com.hse.course.model;

public enum Interest {
    GAME(1, "Игры"),
    VOLLEYBALL(2,"Волейбол"),
    FOOD(3,"Еда");


    private final int value;
    private final String translation;

    Interest(int value, String translation) {
        this.value = value;
        this.translation = translation;
    }

    public int getValue() {
        return value;
    }

    public String getTranslation() {
        return translation;
    }
}
