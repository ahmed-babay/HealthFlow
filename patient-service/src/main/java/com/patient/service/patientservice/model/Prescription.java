package com.patient.service.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @NotNull
    @Column(name = "medication_name", length = 200)
    private String medicationName;

    @Column(name = "medication_code", length = 50)
    private String medicationCode; // Generic drug code

    @NotNull
    @Column(name = "dosage", length = 100)
    private String dosage;

    @NotNull
    @Column(name = "frequency", length = 100)
    private String frequency; //"3 times daily", "Every 8 hours"

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "instructions", length = 500)
    private String instructions;

    @NotNull
    @Column(name = "prescribed_date")
    private LocalDate prescribedDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PrescriptionStatus status;

    @NotNull
    @Column(name = "prescribing_doctor")
    private String prescribingDoctor;

    @Column(name = "notes", length = 1000)
    private String notes;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum PrescriptionStatus {
        ACTIVE, COMPLETED, DISCONTINUED, EXPIRED
    }

    // Constructors
    public Prescription() {
        this.createdAt = LocalDateTime.now();
        this.prescribedDate = LocalDate.now();
        this.status = PrescriptionStatus.ACTIVE;
    }

    public Prescription(MedicalRecord medicalRecord, String medicationName, String dosage, 
                       String frequency, String prescribingDoctor) {
        this();
        this.medicalRecord = medicalRecord;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.prescribingDoctor = prescribingDoctor;
        this.startDate = LocalDate.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

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
        if (durationDays != null && this.startDate != null) {
            this.endDate = this.startDate.plusDays(durationDays);
        }
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

    public LocalDate getPrescribedDate() {
        return prescribedDate;
    }

    public void setPrescribedDate(LocalDate prescribedDate) {
        this.prescribedDate = prescribedDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public String getPrescribingDoctor() {
        return prescribingDoctor;
    }

    public void setPrescribingDoctor(String prescribingDoctor) {
        this.prescribingDoctor = prescribingDoctor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

