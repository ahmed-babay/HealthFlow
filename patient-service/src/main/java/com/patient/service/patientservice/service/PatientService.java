package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.mapper.PatientMapper;
import com.patient.service.patientservice.model.Patient;
import com.patient.service.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }
}
