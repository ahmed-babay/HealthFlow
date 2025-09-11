package com.patient.service.patientservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Validator for ICD-10 diagnosis codes
 * Ensures medical accuracy and proper formatting
 */
public class ICD10CodeValidator implements ConstraintValidator<ValidICD10Code, String> {

    private boolean allowEmpty;
    
    // Common ICD-10 code prefixes for validation
    private static final Set<String> VALID_ICD10_PREFIXES = new HashSet<>(Arrays.asList(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    ));
    
    // Common medical ICD-10 codes for future reference
    // Can be used for enhanced validation or suggestions
    @SuppressWarnings("unused")
    private static final Set<String> COMMON_MEDICAL_CODES = new HashSet<>(Arrays.asList(
        "T78.02", "T78.02XA", "T78.40", "T78.40XA", // Anaphylactic reactions
        "M79.3", "M79.30", "M79.31",                 // Muscle pain
        "R50.9", "R50.90",                           // Fever
        "Z51.89", "Z51.11",                          // Encounter for other care
        "I10", "I10.9",                              // Hypertension
        "E11.9", "E11.90"                            // Type 2 diabetes
    ));

    @Override
    public void initialize(ValidICD10Code constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        // Handle null/empty cases
        if (code == null || code.trim().isEmpty()) {
            return allowEmpty;
        }

        code = code.trim().toUpperCase();

        // Basic ICD-10 format validation
        if (!isValidICD10Format(code)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "ICD-10 code must start with a letter followed by 2 digits (e.g., M79, T78.02XA). Found: " + code)
                .addConstraintViolation();
            return false;
        }

        // Check if it's a known valid prefix
        String prefix = code.substring(0, 1);
        if (!VALID_ICD10_PREFIXES.contains(prefix)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "ICD-10 code starts with invalid letter: " + prefix + ". Must be A-Z.")
                .addConstraintViolation();
            return false;
        }

        // Additional medical validation
        if (!isValidMedicalCode(code)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "ICD-10 code format is invalid for medical use: " + code)
                .addConstraintViolation();
            return false;
        }

        return true;
    }

    /**
     * Validates basic ICD-10 format: Letter + 2 digits + optional extensions
     */
    private boolean isValidICD10Format(String code) {
        // ICD-10 pattern: Letter + 2 digits + optional decimal + 1-4 more characters
        return code.matches("^[A-Z]\\d{2}(\\.\\d{1,2}[A-Z]*)?$");
    }

    /**
     * Additional medical validation for common healthcare scenarios
     */
    private boolean isValidMedicalCode(String code) {
        // Check for common invalid patterns
        if (code.startsWith("U")) {
            // U codes are special purpose codes, less common in general practice
            return code.matches("^U\\d{2}(\\.\\d{1,2})?$");
        }

        // Validate specific medical code ranges
        char firstLetter = code.charAt(0);
        String numberPart = code.substring(1, 3);
        int codeNumber;
        
        try {
            codeNumber = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return false;
        }

        // Validate code ranges for specific categories
        switch (firstLetter) {
            case 'A':
            case 'B':
                // Infectious diseases: A00-B99
                return codeNumber >= 0 && codeNumber <= 99;
            case 'C':
            case 'D':
                // Neoplasms: C00-D49
                return (firstLetter == 'C' && codeNumber >= 0 && codeNumber <= 99) ||
                       (firstLetter == 'D' && codeNumber >= 0 && codeNumber <= 49);
            case 'T':
                // Injuries and poisoning: T07-T88
                return codeNumber >= 7 && codeNumber <= 88;
            default:
                // For other categories, basic format validation is sufficient
                return true;
        }
    }
}
