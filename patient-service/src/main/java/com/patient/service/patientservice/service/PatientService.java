package com.patient.service.patientservice.service;

import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.exception.EmailAlreadyExistsException;
import com.patient.service.patientservice.exception.PatientNotFoundException;
import com.patient.service.patientservice.mapper.PatientMapper;
import com.patient.service.patientservice.model.Patient;
import com.patient.service.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Cacheable(value = "patients", key = "'all-patients'")
    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientMapper::toDTO).toList();
    }

    @Cacheable(value = "patients", key = "#id.toString()")
    public PatientResponseDTO getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
        return PatientMapper.toDTO(patient);
    }

    @CacheEvict(value = "patients", key = "'all-patients'")
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException("Patient with this Email" + "already exists"+ patientRequestDTO.getEmail());
        }
        Patient newPatient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    @Caching(evict = {
        @CacheEvict(value = "patients", key = "'all-patients'"),
        @CacheEvict(value = "patients", key = "#id.toString()")
    })
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with ID: "+ id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id))
            throw new EmailAlreadyExistsException("Patient with this Email" + "already exists"+ patientRequestDTO.getEmail());

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);

    }


    @Caching(evict = {
        @CacheEvict(value = "patients", key = "'all-patients'"),
        @CacheEvict(value = "patients", key = "#id.toString()"),
        @CacheEvict(value = "allergies", key = "#id.toString() + '-allergies'"),
        @CacheEvict(value = "medical-records", key = "#id.toString() + '-records'")
    })
    public void deletePatient(UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("Patient not found with ID: "+id));
        patientRepository.delete(patient);
        System.out.println("Deleted Patient with ID: "+id);
    }
}
