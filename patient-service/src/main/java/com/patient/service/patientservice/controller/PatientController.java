package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.mapper.PatientMapper;
import com.patient.service.patientservice.model.Patient;
import com.patient.service.patientservice.repository.PatientRepository;
import com.patient.service.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> savePatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO =patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,@Valid @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO =patientService.updatePatient(id,patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id,@Valid PatientRequestDTO patientRequestDTO){
        patientService.deletePatient(id,patientRequestDTO);
        return ResponseEntity.ok().build();
    }
}
