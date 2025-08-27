package com.patient.service.patientservice.mapper;

import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.model.Patient;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient p){
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(p.getId().toString());
        dto.setName(p.getName());
        dto.setAddress(p.getAddress());
        dto.setEmail(p.getEmail());
        dto.setDateOfBirth(p.getDateOfBirth().toString());
        return dto;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        return patient;
    }
}
