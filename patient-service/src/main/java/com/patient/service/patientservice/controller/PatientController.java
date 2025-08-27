package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
