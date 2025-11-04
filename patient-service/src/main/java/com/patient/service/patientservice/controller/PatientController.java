package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.AllergyRequestDTO;
import com.patient.service.patientservice.dto.AllergyResponseDTO;
import com.patient.service.patientservice.dto.PatientRequestDTO;
import com.patient.service.patientservice.dto.PatientResponseDTO;
import com.patient.service.patientservice.service.AllergyService;
import com.patient.service.patientservice.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;
    private final AllergyService allergyService;

    public PatientController(PatientService patientService, AllergyService allergyService) {
        this.patientService = patientService;
        this.allergyService = allergyService;
    }

    /**
     * Health check endpoint (no authentication required)
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "patient-service");
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

    @GetMapping("/email")
    public ResponseEntity<PatientResponseDTO> getPatientByEmail(@RequestParam String email){
        PatientResponseDTO patientResponseDTO = patientService.getPatientByEmail(email);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id){
        PatientResponseDTO patientResponseDTO = patientService.getPatientById(id);
        return ResponseEntity.ok().body(patientResponseDTO);
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

    // Patient Allergy endpoints
    @GetMapping("/{id}/allergies")
    public ResponseEntity<List<AllergyResponseDTO>> getAllergiesByPatient(@PathVariable UUID id) {
        List<AllergyResponseDTO> allergies = allergyService.getAllergiesByPatientId(id);
        return ResponseEntity.ok().body(allergies);
    }

    @GetMapping("/{id}/allergies/active")
    public ResponseEntity<List<AllergyResponseDTO>> getActiveAllergiesByPatient(@PathVariable UUID id) {
        List<AllergyResponseDTO> allergies = allergyService.getActiveAllergiesByPatientId(id);
        return ResponseEntity.ok().body(allergies);
    }

    @GetMapping("/{id}/allergies/severe")
    public ResponseEntity<List<AllergyResponseDTO>> getSevereAllergiesByPatient(@PathVariable UUID id) {
        List<AllergyResponseDTO> allergies = allergyService.getSevereAllergiesByPatientId(id);
        return ResponseEntity.ok().body(allergies);
    }

    @GetMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<AllergyResponseDTO> getAllergyById(@PathVariable UUID patientId, @PathVariable UUID allergyId) {
        AllergyResponseDTO allergy = allergyService.getAllergyById(patientId, allergyId);
        return ResponseEntity.ok().body(allergy);
    }

    @GetMapping("/{id}/allergies/count")
    public ResponseEntity<Long> getAllergyCountByPatient(@PathVariable UUID id) {
        long count = allergyService.getAllergyCountByPatient(id);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/{id}/allergies/count/active")
    public ResponseEntity<Long> getActiveAllergyCountByPatient(@PathVariable UUID id) {
        long count = allergyService.getActiveAllergyCountByPatient(id);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping("/{id}/allergies")
    public ResponseEntity<AllergyResponseDTO> createAllergy(@PathVariable UUID id, 
                                                           @Valid @RequestBody AllergyRequestDTO requestDTO) {
        AllergyResponseDTO allergy = allergyService.createAllergy(id, requestDTO);
        return ResponseEntity.ok().body(allergy);
    }

    @PutMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<AllergyResponseDTO> updateAllergy(@PathVariable UUID patientId,
                                                           @PathVariable UUID allergyId,
                                                           @Valid @RequestBody AllergyRequestDTO requestDTO) {
        AllergyResponseDTO allergy = allergyService.updateAllergy(patientId, allergyId, requestDTO);
        return ResponseEntity.ok().body(allergy);
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable UUID patientId, @PathVariable UUID allergyId) {
        allergyService.deleteAllergy(patientId, allergyId);
        return ResponseEntity.ok().build();
    }
}
