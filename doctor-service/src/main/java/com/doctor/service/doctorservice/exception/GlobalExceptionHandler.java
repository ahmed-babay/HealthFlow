package com.doctor.service.doctorservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the Doctor service
 * Handles exceptions and converts them to appropriate HTTP responses
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DoctorNotFoundException
     * Returns 404 Not Found with error details
     */
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDoctorNotFoundException(DoctorNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Doctor Not Found");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", "/api/v1/doctors");

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles IllegalArgumentException (business rule violations)
     * Returns 400 Bad Request with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", "/api/v1/doctors");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles validation errors from @Valid annotations
     * Returns 400 Bad Request with detailed field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> fieldErrors = new HashMap<>();

        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Validation Failed");
        errorResponse.put("message", "Request validation failed");
        errorResponse.put("fieldErrors", fieldErrors);
        errorResponse.put("path", "/api/v1/doctors");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other unexpected exceptions
     * Returns 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "An unexpected error occurred");
        errorResponse.put("path", "/api/v1/doctors");

        // Log the actual exception for debugging (in real app, use proper logging)
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
