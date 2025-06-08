package com.example.backend.validation;

public class AccommodationException extends RuntimeException {
    public AccommodationException() {
        super();
    }

    public AccommodationException(String message) {
        super(message);
    }
}
