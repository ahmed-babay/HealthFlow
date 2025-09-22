package com.doctor.service.doctorservice.exception;

/**
 * Exception thrown when a doctor is not found in the system
 * Used in service layer when doctor lookup operations fail
 */
public class DoctorNotFoundException extends RuntimeException {
    
    public DoctorNotFoundException(String message) {
        super(message);
    }
    
    public DoctorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
