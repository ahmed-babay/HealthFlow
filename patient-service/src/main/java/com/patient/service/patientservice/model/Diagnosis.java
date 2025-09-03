package com.patient.service.patientservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "diagnoses")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @NotNull
    @Column(name = "diagnosis_code", length = 20)
    private String diagnosisCode; // ICD-10 code

    @NotNull
    @Column(name = "diagnosis_name", length = 200)
    private String diagnosisName;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DiagnosisStatus status;

    @Column(name = "diagnosed_date")
    private LocalDateTime diagnosedDate;

    @Column(name = "notes", length = 1000)
    private String notes;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Severity {
        MILD, MODERATE, SEVERE, CRITICAL
    }

    public enum DiagnosisStatus {
        ACTIVE, RESOLVED, CHRONIC, UNDER_INVESTIGATION
    }

    // Constructors
    public Diagnosis() {
        this.createdAt = LocalDateTime.now();
        this.diagnosedDate = LocalDateTime.now();
        this.status = DiagnosisStatus.ACTIVE;
    }

    public Diagnosis(MedicalRecord medicalRecord, String diagnosisCode, String diagnosisName) {
        this();
        this.medicalRecord = medicalRecord;
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
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

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public DiagnosisStatus getStatus() {
        return status;
    }

    public void setStatus(DiagnosisStatus status) {
        this.status = status;
    }

    public LocalDateTime getDiagnosedDate() {
        return diagnosedDate;
    }

    public void setDiagnosedDate(LocalDateTime diagnosedDate) {
        this.diagnosedDate = diagnosedDate;
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
}
