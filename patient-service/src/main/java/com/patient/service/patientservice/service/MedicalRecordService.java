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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable(value = "medical-records", key = "#patientId.toString() + '-records'")
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

    @Cacheable(value = "medical-records", key = "#id.toString()")
    public MedicalRecordResponseDTO getMedicalRecordById(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        return MedicalRecordMapper.toDTO(medicalRecord);
    }

    @CacheEvict(value = "medical-records", key = "#requestDTO.getPatientId().toString() + '-records'")
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO requestDTO) {
        // Validate patient exists
        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDTO.getPatientId()));

        MedicalRecord medicalRecord = MedicalRecordMapper.toModel(requestDTO, patient);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        return MedicalRecordMapper.toDTO(savedRecord);
    }

    @Caching(evict = {
        @CacheEvict(value = "medical-records", key = "#id.toString()"),
        @CacheEvict(value = "medical-records", key = "#requestDTO.getPatientId().toString() + '-records'")
    })
    public MedicalRecordResponseDTO updateMedicalRecord(UUID id, MedicalRecordRequestDTO requestDTO) {
        MedicalRecord existingRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));

        MedicalRecordMapper.updateModel(existingRecord, requestDTO);
        MedicalRecord updatedRecord = medicalRecordRepository.save(existingRecord);
        return MedicalRecordMapper.toDTO(updatedRecord);
    }

    @CacheEvict(value = "medical-records", key = "#id.toString()")
    public void deleteMedicalRecord(UUID id) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        
        // Clear related caches manually through cache manager if needed
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


