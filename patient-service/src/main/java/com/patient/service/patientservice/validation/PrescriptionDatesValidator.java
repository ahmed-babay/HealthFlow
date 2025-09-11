package com.patient.service.patientservice.validation;

import com.patient.service.patientservice.dto.PrescriptionRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Validator for prescription date ranges and duration limits
 * Ensures medical safety and regulatory compliance
 */
public class PrescriptionDatesValidator implements ConstraintValidator<ValidPrescriptionDates, PrescriptionRequestDTO> {

    private int maxDurationDays;

    @Override
    public void initialize(ValidPrescriptionDates constraintAnnotation) {
        this.maxDurationDays = constraintAnnotation.maxDurationDays();
    }

    @Override
    public boolean isValid(PrescriptionRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        // Validate duration days
        if (dto.getDurationDays() != null) {
            if (dto.getDurationDays() <= 0) {
                context.buildConstraintViolationWithTemplate(
                    "Prescription duration must be greater than 0 days")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                isValid = false;
            } else if (dto.getDurationDays() > maxDurationDays) {
                context.buildConstraintViolationWithTemplate(
                    "Prescription duration cannot exceed " + maxDurationDays + " days for safety reasons")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                isValid = false;
            }
        }

        // Validate start and end dates if provided
        LocalDate startDate = parseDate(dto);  // Will be set to today if not provided
        LocalDate endDate = calculateEndDate(dto, startDate);

        if (startDate != null && endDate != null) {
            // End date must be after start date
            if (!endDate.isAfter(startDate)) {
                context.buildConstraintViolationWithTemplate(
                    "Prescription end date must be after start date")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                isValid = false;
            }

            // Check if calculated duration matches provided duration
            if (dto.getDurationDays() != null) {
                long calculatedDays = ChronoUnit.DAYS.between(startDate, endDate);
                if (Math.abs(calculatedDays - dto.getDurationDays()) > 1) { // Allow 1 day tolerance
                    context.buildConstraintViolationWithTemplate(
                        "Duration days (" + dto.getDurationDays() + ") doesn't match calculated days between dates (" + calculatedDays + ")")
                        .addPropertyNode("durationDays")
                        .addConstraintViolation();
                    isValid = false;
                }
            }

            // Validate prescription isn't too far in the future
            LocalDate maxFutureDate = LocalDate.now().plusDays(30); // Max 30 days in future
            if (startDate.isAfter(maxFutureDate)) {
                context.buildConstraintViolationWithTemplate(
                    "Prescription start date cannot be more than 30 days in the future")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                isValid = false;
            }
        }

        // Validate medication-specific duration limits
        if (!isValid && dto.getMedicationName() != null) {
            isValid = validateMedicationSpecificRules(dto, context);
        }

        return isValid;
    }

    /**
     * Parse start date from DTO or default to today
     */
    private LocalDate parseDate(PrescriptionRequestDTO dto) {
        // In our current implementation, start date is set automatically to today
        // This method provides flexibility for future enhancements
        return LocalDate.now();
    }

    /**
     * Calculate end date based on start date and duration
     */
    private LocalDate calculateEndDate(PrescriptionRequestDTO dto, LocalDate startDate) {
        if (dto.getDurationDays() != null && startDate != null) {
            return startDate.plusDays(dto.getDurationDays());
        }
        return null;
    }

    /**
     * Apply medication-specific validation rules
     */
    private boolean validateMedicationSpecificRules(PrescriptionRequestDTO dto, ConstraintValidatorContext context) {
        String medication = dto.getMedicationName().toLowerCase();
        
        // Controlled substances have stricter limits
        if (isControlledSubstance(medication)) {
            if (dto.getDurationDays() != null && dto.getDurationDays() > 30) {
                context.buildConstraintViolationWithTemplate(
                    "Controlled substances cannot be prescribed for more than 30 days")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                return false;
            }
        }

        // Antibiotics typically have shorter durations
        if (isAntibiotic(medication)) {
            if (dto.getDurationDays() != null && dto.getDurationDays() > 14) {
                context.buildConstraintViolationWithTemplate(
                    "Antibiotic prescriptions typically should not exceed 14 days without special justification")
                    .addPropertyNode("durationDays")
                    .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    /**
     * Check if medication is a controlled substance
     */
    private boolean isControlledSubstance(String medication) {
        // Common controlled substances (simplified list)
        String[] controlledSubstances = {
            "oxycodone", "morphine", "fentanyl", "codeine", "tramadol", 
            "lorazepam", "alprazolam", "diazepam", "adderall", "ritalin"
        };
        
        for (String controlled : controlledSubstances) {
            if (medication.contains(controlled)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if medication is an antibiotic
     */
    private boolean isAntibiotic(String medication) {
        // Common antibiotics (simplified list)
        String[] antibiotics = {
            "amoxicillin", "penicillin", "azithromycin", "ciprofloxacin", 
            "doxycycline", "cephalexin", "clindamycin", "metronidazole"
        };
        
        for (String antibiotic : antibiotics) {
            if (medication.contains(antibiotic)) {
                return true;
            }
        }
        return false;
    }
}
