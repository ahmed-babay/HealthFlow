package com.patient.service.patientservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that prescribed medications don't conflict with patient allergies
 * Critical for patient safety - prevents dangerous drug-allergy interactions
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllergyInteractionValidator.class)
@Documented
public @interface ValidAllergyInteraction {
    
    String message() default "Medication may cause allergic reaction based on patient allergy history";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Whether to perform strict checking (includes drug class interactions)
     */
    boolean strictMode() default true;
}
