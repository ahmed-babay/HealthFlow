package com.patient.service.patientservice.mapper;

import com.patient.service.patientservice.dto.AllergyRequestDTO;
import com.patient.service.patientservice.dto.AllergyResponseDTO;
import com.patient.service.patientservice.model.Allergy;
import com.patient.service.patientservice.model.Patient;

public class AllergyMapper {

    public static AllergyResponseDTO toDTO(Allergy allergy) {
        AllergyResponseDTO dto = new AllergyResponseDTO();
        dto.setId(allergy.getId().toString());
        dto.setPatientId(allergy.getPatient().getId().toString());
        dto.setPatientName(allergy.getPatient().getName());
        dto.setAllergen(allergy.getAllergen());
        
        if (allergy.getAllergyType() != null) {
            dto.setAllergyType(allergy.getAllergyType().toString());
        }
        
        if (allergy.getSeverity() != null) {
            dto.setSeverity(allergy.getSeverity().toString());
        }
        
        dto.setReaction(allergy.getReaction());
        dto.setNotes(allergy.getNotes());
        
        if (allergy.getDiagnosedDate() != null) {
            dto.setDiagnosedDate(allergy.getDiagnosedDate().toString());
        }
        
        dto.setIsActive(allergy.getIsActive());
        dto.setCreatedAt(allergy.getCreatedAt().toString());
        
        if (allergy.getUpdatedAt() != null) {
            dto.setUpdatedAt(allergy.getUpdatedAt().toString());
        }
        
        return dto;
    }

    public static Allergy toModel(AllergyRequestDTO dto, Patient patient) {
        Allergy allergy = new Allergy();
        allergy.setPatient(patient);
        allergy.setAllergen(dto.getAllergen());
        
        if (dto.getAllergyType() != null && !dto.getAllergyType().trim().isEmpty()) {
            allergy.setAllergyType(Allergy.AllergyType.valueOf(dto.getAllergyType().toUpperCase()));
        }
        
        if (dto.getSeverity() != null && !dto.getSeverity().trim().isEmpty()) {
            allergy.setSeverity(Allergy.AllergySeverity.valueOf(dto.getSeverity().toUpperCase()));
        }
        
        allergy.setReaction(dto.getReaction());
        allergy.setNotes(dto.getNotes());
        
        if (dto.getIsActive() != null) {
            allergy.setIsActive(dto.getIsActive());
        }
        
        return allergy;
    }

    public static void updateModel(Allergy existingAllergy, AllergyRequestDTO dto) {
        existingAllergy.setAllergen(dto.getAllergen());
        
        if (dto.getAllergyType() != null && !dto.getAllergyType().trim().isEmpty()) {
            existingAllergy.setAllergyType(Allergy.AllergyType.valueOf(dto.getAllergyType().toUpperCase()));
        }
        
        if (dto.getSeverity() != null && !dto.getSeverity().trim().isEmpty()) {
            existingAllergy.setSeverity(Allergy.AllergySeverity.valueOf(dto.getSeverity().toUpperCase()));
        }
        
        existingAllergy.setReaction(dto.getReaction());
        existingAllergy.setNotes(dto.getNotes());
        
        if (dto.getIsActive() != null) {
            existingAllergy.setIsActive(dto.getIsActive());
        }
    }
}
