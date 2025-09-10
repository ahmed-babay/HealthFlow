package com.patient.service.patientservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates prescription date ranges for medical safety
 * - End date must be after start date
 * - Duration must match calculated days between dates
 * - Maximum prescription length limits
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PrescriptionDatesValidator.class)
@Documented
public @interface ValidPrescriptionDates {
    
    String message() default "Invalid prescription date range";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Maximum allowed prescription duration in days (default 90 days)
     */
    int maxDurationDays() default 90;
}
