package com.patient.service.patientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class AllergyRequestDTO {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotBlank(message = "Allergen is required")
    @Size(max = 200, message = "Allergen cannot exceed 200 characters")
    private String allergen;

    private String allergyType; // FOOD, MEDICATION, ENVIRONMENTAL, CONTACT, INSECT, OTHER

    private String severity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

    @Size(max = 500, message = "Reaction description cannot exceed 500 characters")
    private String reaction;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    private Boolean isActive; // Whether this allergy is still active

    // Constructors
    public AllergyRequestDTO() {}

    public AllergyRequestDTO(UUID patientId, String allergen, String allergyType) {
        this.patientId = patientId;
        this.allergen = allergen;
        this.allergyType = allergyType;
        this.isActive = true;
    }

    // Getters and Setters
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public String getAllergen() {
        return allergen;
    }

    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    public String getAllergyType() {
        return allergyType;
    }

    public void setAllergyType(String allergyType) {
        this.allergyType = allergyType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
