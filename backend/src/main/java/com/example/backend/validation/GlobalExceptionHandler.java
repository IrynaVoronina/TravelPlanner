package com.example.backend.validation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RouteSequenceException.class)
    public ResponseEntity<ExceptionResponse> handleRouteSequenceException(RouteSequenceException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccommodationException.class)
    public ResponseEntity<ExceptionResponse> handleAccommodationException(AccommodationException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> map = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError
                -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
