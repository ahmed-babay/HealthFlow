package com.patient.service.patientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class MedicalRecordRequestDTO {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    @NotBlank(message = "Chief complaint is required")
    @Size(max = 500, message = "Chief complaint cannot exceed 500 characters")
    private String chiefComplaint;

    @Size(max = 1000, message = "Symptoms cannot exceed 1000 characters")
    private String symptoms;

    @Size(max = 2000, message = "Examination notes cannot exceed 2000 characters")
    private String examinationNotes;

    @Size(max = 1000, message = "Treatment plan cannot exceed 1000 characters")
    private String treatmentPlan;

    @Size(max = 1500, message = "Doctor notes cannot exceed 1500 characters")
    private String doctorNotes;

    @NotBlank(message = "Attending doctor is required")
    private String attendingDoctor;

    // Constructors
    public MedicalRecordRequestDTO() {}

    public MedicalRecordRequestDTO(UUID patientId, String chiefComplaint, String attendingDoctor) {
        this.patientId = patientId;
        this.chiefComplaint = chiefComplaint;
        this.attendingDoctor = attendingDoctor;
    }

    // Getters and Setters
    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getExaminationNotes() {
        return examinationNotes;
    }

    public void setExaminationNotes(String examinationNotes) {
        this.examinationNotes = examinationNotes;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }

    public String getAttendingDoctor() {
        return attendingDoctor;
    }

    public void setAttendingDoctor(String attendingDoctor) {
        this.attendingDoctor = attendingDoctor;
    }
}
