package com.patient.service.patientservice.dto;

import com.patient.service.patientservice.validation.ValidICD10Code;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DiagnosisRequestDTO {

    @NotBlank(message = "Diagnosis code is required")
    @Size(max = 20, message = "Diagnosis code cannot exceed 20 characters")
    @ValidICD10Code(message = "Invalid ICD-10 diagnosis code format")
    private String diagnosisCode;

    @NotBlank(message = "Diagnosis name is required")
    @Size(max = 200, message = "Diagnosis name cannot exceed 200 characters")
    private String diagnosisName;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String severity; // MILD, MODERATE, SEVERE, CRITICAL

    private String status; // ACTIVE, RESOLVED, CHRONIC, UNDER_INVESTIGATION

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    // Constructors
    public DiagnosisRequestDTO() {}

    public DiagnosisRequestDTO(String diagnosisCode, String diagnosisName) {
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
    }

    // Getters and Setters
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
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
