package com.example.backend.validation;

public class MealBreakException extends RuntimeException {
    public MealBreakException() {
        super();
    }

    public MealBreakException(String message) {
        super(message);
    }
}
