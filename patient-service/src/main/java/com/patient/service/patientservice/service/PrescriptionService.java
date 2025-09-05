package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.PrescriptionRequestDTO;
import com.patient.service.patientservice.dto.PrescriptionResponseDTO;
import com.patient.service.patientservice.exception.MedicalRecordNotFoundException;
import com.patient.service.patientservice.mapper.PrescriptionMapper;
import com.patient.service.patientservice.model.MedicalRecord;
import com.patient.service.patientservice.model.Prescription;
import com.patient.service.patientservice.repository.MedicalRecordRepository;
import com.patient.service.patientservice.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository, 
                              MedicalRecordRepository medicalRecordRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<PrescriptionResponseDTO> getPrescriptionsByMedicalRecord(UUID medicalRecordId) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecordIdOrderByPrescribedDateDesc(medicalRecordId);
        return prescriptions.stream()
                .map(PrescriptionMapper::toDTO)
                .toList();
    }

    public PrescriptionResponseDTO createPrescription(UUID medicalRecordId, PrescriptionRequestDTO requestDTO) {
        // Validate medical record exists
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId));

        Prescription prescription = PrescriptionMapper.toModel(requestDTO, medicalRecord);
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return PrescriptionMapper.toDTO(savedPrescription);
    }

    public PrescriptionResponseDTO updatePrescription(UUID medicalRecordId, UUID prescriptionId, PrescriptionRequestDTO requestDTO) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        Prescription existingPrescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));

        // Validate prescription belongs to the medical record
        if (!existingPrescription.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new RuntimeException("Prescription does not belong to the specified medical record");
        }

        PrescriptionMapper.updateModel(existingPrescription, requestDTO);
        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);
        return PrescriptionMapper.toDTO(updatedPrescription);
    }

    public void deletePrescription(UUID medicalRecordId, UUID prescriptionId) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found with ID: " + prescriptionId));

        // Validate prescription belongs to the medical record
        if (!prescription.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new RuntimeException("Prescription does not belong to the specified medical record");
        }

        prescriptionRepository.delete(prescription);
        System.out.println("Deleted Prescription with ID: " + prescriptionId);
    }

    public List<PrescriptionResponseDTO> getActivePrescriptionsByMedicalRecord(UUID medicalRecordId) {
        // Validate medical record exists
        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new MedicalRecordNotFoundException("Medical record not found with ID: " + medicalRecordId);
        }

        List<Prescription> prescriptions = prescriptionRepository.findByMedicalRecordIdAndStatusOrderByPrescribedDateDesc(
                medicalRecordId, Prescription.PrescriptionStatus.ACTIVE);
        return prescriptions.stream()
                .map(PrescriptionMapper::toDTO)
                .toList();
    }
}
