package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.AllergyRequestDTO;
import com.patient.service.patientservice.dto.AllergyResponseDTO;
import com.patient.service.patientservice.exception.PatientNotFoundException;
import com.patient.service.patientservice.mapper.AllergyMapper;
import com.patient.service.patientservice.model.Allergy;
import com.patient.service.patientservice.model.Patient;
import com.patient.service.patientservice.repository.AllergyRepository;
import com.patient.service.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AllergyService {

    private final AllergyRepository allergyRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AllergyService(AllergyRepository allergyRepository, PatientRepository patientRepository) {
        this.allergyRepository = allergyRepository;
        this.patientRepository = patientRepository;
    }

    public List<AllergyResponseDTO> getAllergiesByPatientId(UUID patientId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        List<Allergy> allergies = allergyRepository.findByPatientIdOrderByDiagnosedDateDesc(patientId);
        return allergies.stream()
                .map(AllergyMapper::toDTO)
                .toList();
    }

    public List<AllergyResponseDTO> getActiveAllergiesByPatientId(UUID patientId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        List<Allergy> allergies = allergyRepository.findByPatientIdAndIsActiveOrderByDiagnosedDateDesc(patientId, true);
        return allergies.stream()
                .map(AllergyMapper::toDTO)
                .toList();
    }

    public List<AllergyResponseDTO> getSevereAllergiesByPatientId(UUID patientId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        List<Allergy> allergies = allergyRepository.findSevereActiveAllergies(patientId);
        return allergies.stream()
                .map(AllergyMapper::toDTO)
                .toList();
    }

    public AllergyResponseDTO getAllergyById(UUID patientId, UUID allergyId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new RuntimeException("Allergy not found with ID: " + allergyId));

        // Validate allergy belongs to the patient
        if (!allergy.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Allergy does not belong to the specified patient");
        }

        return AllergyMapper.toDTO(allergy);
    }

    public AllergyResponseDTO createAllergy(UUID patientId, AllergyRequestDTO requestDTO) {
        // Validate patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + patientId));

        // Override patientId in requestDTO to ensure consistency
        requestDTO.setPatientId(patientId);

        Allergy allergy = AllergyMapper.toModel(requestDTO, patient);
        Allergy savedAllergy = allergyRepository.save(allergy);
        return AllergyMapper.toDTO(savedAllergy);
    }

    public AllergyResponseDTO updateAllergy(UUID patientId, UUID allergyId, AllergyRequestDTO requestDTO) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        Allergy existingAllergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new RuntimeException("Allergy not found with ID: " + allergyId));

        // Validate allergy belongs to the patient
        if (!existingAllergy.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Allergy does not belong to the specified patient");
        }

        AllergyMapper.updateModel(existingAllergy, requestDTO);
        Allergy updatedAllergy = allergyRepository.save(existingAllergy);
        return AllergyMapper.toDTO(updatedAllergy);
    }

    public void deleteAllergy(UUID patientId, UUID allergyId) {
        // Validate patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }

        Allergy allergy = allergyRepository.findById(allergyId)
                .orElseThrow(() -> new RuntimeException("Allergy not found with ID: " + allergyId));

        // Validate allergy belongs to the patient
        if (!allergy.getPatient().getId().equals(patientId)) {
            throw new RuntimeException("Allergy does not belong to the specified patient");
        }

        allergyRepository.delete(allergy);
        System.out.println("Deleted Allergy with ID: " + allergyId + " for Patient: " + patientId);
    }

    public long getAllergyCountByPatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }
        return allergyRepository.countByPatientId(patientId);
    }

    public long getActiveAllergyCountByPatient(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }
        return allergyRepository.countByPatientIdAndIsActive(patientId, true);
    }
}
