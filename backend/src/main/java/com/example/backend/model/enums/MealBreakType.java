package com.example.backend.model.enums;

import lombok.Getter;

@Getter
public enum MealBreakType {

    BREAKFAST("Morning meal"),
    LUNCH("Midday meal"),
    DINNER("Evening meal");

    private final String description;

    MealBreakType(String description) {
        this.description = description;
    }
}
