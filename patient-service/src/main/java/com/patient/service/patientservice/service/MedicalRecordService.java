package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.MedicalRecordRequestDTO;
import com.patient.service.patientservice.dto.MedicalRecordResponseDTO;
import com.patient.service.patientservice.exception.MedicalRecordNotFoundException;
import com.patient.service.patientservice.exception.PatientNotFoundException;
import com.patient.service.patientservice.mapper.MedicalRecordMapper;
import com.patient.service.patientservice.model.MedicalRecord;
import com.patient.service.patientservice.model.Patient;
import com.patient.service.patientservice.repository.MedicalRecordRepository;
import com.patient.service.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, 
                               PatientRepository patientRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
    }

    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return medicalRecords.stream()
                .map(MedicalRecordMapper::toDTO)
                .toList();
    }

    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(UUID patientId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }
        
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId);
        return medicalRecords.stream()
                .map(MedicalRecordMapper::toDTO)
                .toList();
    }

    public MedicalRecordResponseDTO getMedicalRecordById(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        return MedicalRecordMapper.toDTO(medicalRecord);
    }

    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO requestDTO) {
        // Validate patient exists
        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDTO.getPatientId()));

        MedicalRecord medicalRecord = MedicalRecordMapper.toModel(requestDTO, patient);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        return MedicalRecordMapper.toDTO(savedRecord);
    }

    public MedicalRecordResponseDTO updateMedicalRecord(UUID id, MedicalRecordRequestDTO requestDTO) {
        MedicalRecord existingRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));

        MedicalRecordMapper.updateModel(existingRecord, requestDTO);
        MedicalRecord updatedRecord = medicalRecordRepository.save(existingRecord);
        return MedicalRecordMapper.toDTO(updatedRecord);
    }

    public void deleteMedicalRecord(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        
        medicalRecordRepository.delete(medicalRecord);
        System.out.println("Deleted Medical Record with ID: " + id);
    }

    public long getMedicalRecordCountByPatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }
        return medicalRecordRepository.countByPatientId(patientId);
    }
}


