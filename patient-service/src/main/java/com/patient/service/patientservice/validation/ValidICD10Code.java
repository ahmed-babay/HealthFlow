package com.patient.service.patientservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates ICD-10 diagnosis codes for medical accuracy
 * ICD-10 format: Letter + 2 digits + optional decimal + 1-2 more characters
 * Examples: "M79.3", "T78.02XA", "Z51.89"
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ICD10CodeValidator.class)
@Documented
public @interface ValidICD10Code {
    
    String message() default "Invalid ICD-10 diagnosis code format. Expected format: Letter + 2 digits + optional decimal + additional characters (e.g., M79.3, T78.02XA)";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Whether to allow empty/null values
     */
    boolean allowEmpty() default true;
}
