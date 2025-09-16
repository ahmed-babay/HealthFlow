package com.patient.service.patientservice.dto;

import com.patient.service.patientservice.validation.ValidAllergyInteraction;
import com.patient.service.patientservice.validation.ValidPrescriptionDates;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@ValidPrescriptionDates(maxDurationDays = 90)
@ValidAllergyInteraction(strictMode = true)
public class PrescriptionRequestDTO {

    @NotBlank(message = "Medication name is required")
    @Size(max = 200, message = "Medication name cannot exceed 200 characters")
    private String medicationName;

    @Size(max = 50, message = "Medication code cannot exceed 50 characters")
    private String medicationCode;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage cannot exceed 100 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 100, message = "Frequency cannot exceed 100 characters")
    private String frequency;

    @Min(value = 1, message = "Duration must be at least 1 day")
    private Integer durationDays;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 500, message = "Instructions cannot exceed 500 characters")
    private String instructions;

    @NotBlank(message = "Prescribing doctor is required")
    private String prescribingDoctor;

    private String status; // ACTIVE, COMPLETED, DISCONTINUED, EXPIRED

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    // Constructors
    public PrescriptionRequestDTO() {}

    public PrescriptionRequestDTO(String medicationName, String dosage, String frequency, String prescribingDoctor) {
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.prescribingDoctor = prescribingDoctor;
    }

    // Getters and Setters
    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getMedicationCode() {
        return medicationCode;
    }

    public void setMedicationCode(String medicationCode) {
        this.medicationCode = medicationCode;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
