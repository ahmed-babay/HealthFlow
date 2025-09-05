package com.patient.service.patientservice.mapper;

import com.patient.service.patientservice.dto.PrescriptionRequestDTO;
import com.patient.service.patientservice.dto.PrescriptionResponseDTO;
import com.patient.service.patientservice.model.MedicalRecord;
import com.patient.service.patientservice.model.Prescription;

import java.time.LocalDate;

public class PrescriptionMapper {

    public static PrescriptionResponseDTO toDTO(Prescription prescription) {
        PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
        dto.setId(prescription.getId().toString());
        dto.setMedicalRecordId(prescription.getMedicalRecord().getId().toString());
        dto.setMedicationName(prescription.getMedicationName());
        dto.setMedicationCode(prescription.getMedicationCode());
        dto.setDosage(prescription.getDosage());
        dto.setFrequency(prescription.getFrequency());
        dto.setDurationDays(prescription.getDurationDays());
        dto.setQuantity(prescription.getQuantity());
        dto.setInstructions(prescription.getInstructions());
        if (prescription.getPrescribedDate() != null) {
            dto.setPrescribedDate(prescription.getPrescribedDate().toString());
        }
        if (prescription.getStartDate() != null) {
            dto.setStartDate(prescription.getStartDate().toString());
        }
        if (prescription.getEndDate() != null) {
            dto.setEndDate(prescription.getEndDate().toString());
        }
        if (prescription.getStatus() != null) {
            dto.setStatus(prescription.getStatus().toString());
        }
        dto.setPrescribingDoctor(prescription.getPrescribingDoctor());
        dto.setNotes(prescription.getNotes());
        dto.setCreatedAt(prescription.getCreatedAt().toString());
        if (prescription.getUpdatedAt() != null) {
            dto.setUpdatedAt(prescription.getUpdatedAt().toString());
        }
        return dto;
    }

    public static Prescription toModel(PrescriptionRequestDTO dto, MedicalRecord medicalRecord) {
        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(medicalRecord);
        prescription.setMedicationName(dto.getMedicationName());
        prescription.setMedicationCode(dto.getMedicationCode());
        prescription.setDosage(dto.getDosage());
        prescription.setFrequency(dto.getFrequency());
        prescription.setDurationDays(dto.getDurationDays());
        prescription.setQuantity(dto.getQuantity());
        prescription.setInstructions(dto.getInstructions());
        prescription.setPrescribingDoctor(dto.getPrescribingDoctor());
        
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            prescription.setStatus(Prescription.PrescriptionStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        prescription.setNotes(dto.getNotes());
        prescription.setStartDate(LocalDate.now());
        
        // Calculate end date if duration is provided
        if (dto.getDurationDays() != null && dto.getDurationDays() > 0) {
            prescription.setEndDate(LocalDate.now().plusDays(dto.getDurationDays()));
        }
        
        return prescription;
    }

    public static void updateModel(Prescription existingPrescription, PrescriptionRequestDTO dto) {
        existingPrescription.setMedicationName(dto.getMedicationName());
        existingPrescription.setMedicationCode(dto.getMedicationCode());
        existingPrescription.setDosage(dto.getDosage());
        existingPrescription.setFrequency(dto.getFrequency());
        existingPrescription.setDurationDays(dto.getDurationDays());
        existingPrescription.setQuantity(dto.getQuantity());
        existingPrescription.setInstructions(dto.getInstructions());
        existingPrescription.setPrescribingDoctor(dto.getPrescribingDoctor());
        
        if (dto.getStatus() != null && !dto.getStatus().trim().isEmpty()) {
            existingPrescription.setStatus(Prescription.PrescriptionStatus.valueOf(dto.getStatus().toUpperCase()));
        }
        
        existingPrescription.setNotes(dto.getNotes());
        
        // Recalculate end date if duration is updated
        if (dto.getDurationDays() != null && dto.getDurationDays() > 0 && existingPrescription.getStartDate() != null) {
            existingPrescription.setEndDate(existingPrescription.getStartDate().plusDays(dto.getDurationDays()));
        }
    }
}
