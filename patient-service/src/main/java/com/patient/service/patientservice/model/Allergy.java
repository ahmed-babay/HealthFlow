package com.patient.service.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "allergies")
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @Column(name = "allergen", length = 200)
    private String allergen; // What the patient is allergic to

    @Enumerated(EnumType.STRING)
    @Column(name = "allergy_type")
    private AllergyType allergyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private AllergySeverity severity;

    @Column(name = "reaction", length = 500)
    private String reaction; // Description of the allergic reaction

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "diagnosed_date")
    private LocalDateTime diagnosedDate;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Enums
    public enum AllergyType {
        FOOD, MEDICATION, ENVIRONMENTAL, CONTACT, INSECT, OTHER
    }

    public enum AllergySeverity {
        MILD, MODERATE, SEVERE, LIFE_THREATENING
    }

    // Constructors
    public Allergy() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
        this.diagnosedDate = LocalDateTime.now();
    }

    public Allergy(Patient patient, String allergen, AllergyType allergyType) {
        this();
        this.patient = patient;
        this.allergen = allergen;
        this.allergyType = allergyType;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getAllergen() {
        return allergen;
    }

    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    public AllergyType getAllergyType() {
        return allergyType;
    }

    public void setAllergyType(AllergyType allergyType) {
        this.allergyType = allergyType;
    }

    public AllergySeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AllergySeverity severity) {
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

    public LocalDateTime getDiagnosedDate() {
        return diagnosedDate;
    }

    public void setDiagnosedDate(LocalDateTime diagnosedDate) {
        this.diagnosedDate = diagnosedDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

