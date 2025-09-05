package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.DiagnosisRequestDTO;
import com.patient.service.patientservice.dto.DiagnosisResponseDTO;
import com.patient.service.patientservice.exception.MedicalRecordNotFoundException;
import com.patient.service.patientservice.mapper.DiagnosisMapper;
import com.patient.service.patientservice.model.Diagnosis;
import com.patient.service.patientservice.model.MedicalRecord;
import com.patient.service.patientservice.repository.DiagnosisRepository;
import com.patient.service.patientservice.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository, 
                           MedicalRecordRepository medicalRecordRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<DiagnosisResponseDTO> getDiagnosesByMedicalRecord(UUID medicalRecordId) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        List<Diagnosis> diagnoses = diagnosisRepository.findByMedicalRecordIdOrderByDiagnosedDateDesc(medicalRecordId);
        return diagnoses.stream()
                .map(DiagnosisMapper::toDTO)
                .toList();
    }

    public DiagnosisResponseDTO createDiagnosis(UUID medicalRecordId, DiagnosisRequestDTO requestDTO) {
        // Validate medical record exists
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId));

        Diagnosis diagnosis = DiagnosisMapper.toModel(requestDTO, medicalRecord);
        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        return DiagnosisMapper.toDTO(savedDiagnosis);
    }

    public DiagnosisResponseDTO updateDiagnosis(UUID medicalRecordId, UUID diagnosisId, DiagnosisRequestDTO requestDTO) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        Diagnosis existingDiagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + diagnosisId));

        // Validate diagnosis belongs to the medical record
        if (!existingDiagnosis.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new RuntimeException("Diagnosis does not belong to the specified medical record");
        }

        DiagnosisMapper.updateModel(existingDiagnosis, requestDTO);
        Diagnosis updatedDiagnosis = diagnosisRepository.save(existingDiagnosis);
        return DiagnosisMapper.toDTO(updatedDiagnosis);
    }

    public void deleteDiagnosis(UUID medicalRecordId, UUID diagnosisId) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found with ID: " + diagnosisId));

        // Validate diagnosis belongs to the medical record
        if (!diagnosis.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new RuntimeException("Diagnosis does not belong to the specified medical record");
        }

        diagnosisRepository.delete(diagnosis);
        System.out.println("Deleted Diagnosis with ID: " + diagnosisId);
    }
}

