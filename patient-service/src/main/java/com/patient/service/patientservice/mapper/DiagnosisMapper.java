package com.patient.service.patientservice.mapper;

import com.patient.service.patientservice.dto.DiagnosisRequestDTO;
import com.patient.service.patientservice.dto.DiagnosisResponseDTO;
import com.patient.service.patientservice.model.Diagnosis;
import com.patient.service.patientservice.model.MedicalRecord;

public class DiagnosisMapper {

    public static DiagnosisResponseDTO toDTO(Diagnosis diagnosis) {
        DiagnosisResponseDTO dto = new DiagnosisResponseDTO();
        dto.setId(diagnosis.getId().toString());
        dto.setMedicalRecordId(diagnosis.getMedicalRecord().getId().toString());
        dto.setDiagnosisCode(diagnosis.getDiagnosisCode());
        dto.setDiagnosisName(diagnosis.getDiagnosisName());
        dto.setDescription(diagnosis.getDescription());
        if (diagnosis.getSeverity() != null) {
            dto.setSeverity(diagnosis.getSeverity().toString());
        }
        if (diagnosis.getStatus() != null) {
            dto.setStatus(diagnosis.getStatus().toString());
        }
        if (diagnosis.getDiagnosedDate() != null) {
            dto.setDiagnosedDate(diagnosis.getDiagnosedDate().toString());
        }
        dto.setNotes(diagnosis.getNotes());
        dto.setCreatedAt(diagnosis.getCreatedAt().toString());
        return dto;
    }

    public static Diagnosis toModel(DiagnosisRequestDTO dto, MedicalRecord medicalRecord) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setMedicalRecord(medicalRecord);
        diagnosis.setDiagnosisCode(dto.getDiagnosisCode());
        diagnosis.setDiagnosisName(dto.getDiagnosisName());
        diagnosis.setDescription(dto.getDescription());
        
        if (dto.getSeverity() != null && !dto.getSeverity().trim().isEmpty()) {
            diagnosis.setSeverity(Diagnosis.Severity.valueOf(dto.getSeverity().toUpperCase()));
        }
        
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            diagnosis.setStatus(Diagnosis.DiagnosisStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        diagnosis.setNotes(dto.getNotes());
        return diagnosis;
    }

    public static void updateModel(Diagnosis existingDiagnosis, DiagnosisRequestDTO dto) {
        existingDiagnosis.setDiagnosisCode(dto.getDiagnosisCode());
        existingDiagnosis.setDiagnosisName(dto.getDiagnosisName());
        existingDiagnosis.setDescription(dto.getDescription());
        
        if (dto.getSeverity() != null && !dto.getSeverity().trim().isEmpty()) {
            existingDiagnosis.setSeverity(Diagnosis.Severity.valueOf(dto.getSeverity().toUpperCase()));
        }
        
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            existingDiagnosis.setStatus(Diagnosis.DiagnosisStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        existingDiagnosis.setNotes(dto.getNotes());
    }
}

