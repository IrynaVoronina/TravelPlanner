package com.example.backend.validation;

public class RouteSequenceException extends RuntimeException {

    public RouteSequenceException(String message) {
        super(message);
    }

    public RouteSequenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
