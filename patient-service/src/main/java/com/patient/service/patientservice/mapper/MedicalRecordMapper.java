package com.patient.service.patientservice.mapper;

import com.patient.service.patientservice.dto.MedicalRecordRequestDTO;
import com.patient.service.patientservice.dto.MedicalRecordResponseDTO;
import com.patient.service.patientservice.model.MedicalRecord;
import com.patient.service.patientservice.model.Patient;

import java.time.LocalDateTime;

public class MedicalRecordMapper {

    public static MedicalRecordResponseDTO toDTO(MedicalRecord medicalRecord) {
        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
        dto.setId(medicalRecord.getId().toString());
        dto.setPatientId(medicalRecord.getPatient().getId().toString());
        dto.setPatientName(medicalRecord.getPatient().getName());
        dto.setRecordDate(medicalRecord.getRecordDate().toString());
        dto.setChiefComplaint(medicalRecord.getChiefComplaint());
        dto.setSymptoms(medicalRecord.getSymptoms());
        dto.setExaminationNotes(medicalRecord.getExaminationNotes());
        dto.setTreatmentPlan(medicalRecord.getTreatmentPlan());
        dto.setDoctorNotes(medicalRecord.getDoctorNotes());
        dto.setAttendingDoctor(medicalRecord.getAttendingDoctor());
        dto.setCreatedAt(medicalRecord.getCreatedAt().toString());
        if (medicalRecord.getUpdatedAt() != null) {
            dto.setUpdatedAt(medicalRecord.getUpdatedAt().toString());
        }
        return dto;
    }

    public static MedicalRecord toModel(MedicalRecordRequestDTO dto, Patient patient) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setChiefComplaint(dto.getChiefComplaint());
        medicalRecord.setSymptoms(dto.getSymptoms());
        medicalRecord.setExaminationNotes(dto.getExaminationNotes());
        medicalRecord.setTreatmentPlan(dto.getTreatmentPlan());
        medicalRecord.setDoctorNotes(dto.getDoctorNotes());
        medicalRecord.setAttendingDoctor(dto.getAttendingDoctor());
        medicalRecord.setRecordDate(LocalDateTime.now());
        return medicalRecord;
    }

    public static void updateModel(MedicalRecord existingRecord, MedicalRecordRequestDTO dto) {
        existingRecord.setChiefComplaint(dto.getChiefComplaint());
        existingRecord.setSymptoms(dto.getSymptoms());
        existingRecord.setExaminationNotes(dto.getExaminationNotes());
        existingRecord.setTreatmentPlan(dto.getTreatmentPlan());
        existingRecord.setDoctorNotes(dto.getDoctorNotes());
        existingRecord.setAttendingDoctor(dto.getAttendingDoctor());
    }
}
