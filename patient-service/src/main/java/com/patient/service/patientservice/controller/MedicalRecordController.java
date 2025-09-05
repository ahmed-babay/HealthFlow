package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.DiagnosisRequestDTO;
import com.patient.service.patientservice.dto.DiagnosisResponseDTO;
import com.patient.service.patientservice.dto.MedicalRecordRequestDTO;
import com.patient.service.patientservice.dto.MedicalRecordResponseDTO;
import com.patient.service.patientservice.service.DiagnosisService;
import com.patient.service.patientservice.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final DiagnosisService diagnosisService;

    public MedicalRecordController(MedicalRecordService medicalRecordService, DiagnosisService diagnosisService) {
        this.medicalRecordService = medicalRecordService;
        this.diagnosisService = diagnosisService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponseDTO>> getAllMedicalRecords() {
        List<MedicalRecordResponseDTO> medicalRecords = medicalRecordService.getAllMedicalRecords();
        return ResponseEntity.ok().body(medicalRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordById(@PathVariable UUID id) {
        MedicalRecordResponseDTO medicalRecord = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok().body(medicalRecord);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecordsByPatientId(@PathVariable UUID patientId) {
        List<MedicalRecordResponseDTO> medicalRecords = medicalRecordService.getMedicalRecordsByPatientId(patientId);
        return ResponseEntity.ok().body(medicalRecords);
    }

    @GetMapping("/patient/{patientId}/count")
    public ResponseEntity<Long> getMedicalRecordCountByPatient(@PathVariable UUID patientId) {
        long count = medicalRecordService.getMedicalRecordCountByPatient(patientId);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> createMedicalRecord(@Valid @RequestBody MedicalRecordRequestDTO requestDTO) {
        MedicalRecordResponseDTO medicalRecord = medicalRecordService.createMedicalRecord(requestDTO);
        return ResponseEntity.ok().body(medicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> updateMedicalRecord(@PathVariable UUID id, 
                                                                       @Valid @RequestBody MedicalRecordRequestDTO requestDTO) {
        MedicalRecordResponseDTO medicalRecord = medicalRecordService.updateMedicalRecord(id, requestDTO);
        return ResponseEntity.ok().body(medicalRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable UUID id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok().build();
    }

    // Nested Diagnosis endpoints
    @GetMapping("/{id}/diagnoses")
    public ResponseEntity<List<DiagnosisResponseDTO>> getDiagnosesByMedicalRecord(@PathVariable UUID id) {
        List<DiagnosisResponseDTO> diagnoses = diagnosisService.getDiagnosesByMedicalRecord(id);
        return ResponseEntity.ok().body(diagnoses);
    }

    @PostMapping("/{id}/diagnoses")
    public ResponseEntity<DiagnosisResponseDTO> createDiagnosis(@PathVariable UUID id, 
                                                               @Valid @RequestBody DiagnosisRequestDTO requestDTO) {
        DiagnosisResponseDTO diagnosis = diagnosisService.createDiagnosis(id, requestDTO);
        return ResponseEntity.ok().body(diagnosis);
    }

    @PutMapping("/{recordId}/diagnoses/{diagnosisId}")
    public ResponseEntity<DiagnosisResponseDTO> updateDiagnosis(@PathVariable UUID recordId,
                                                               @PathVariable UUID diagnosisId,
                                                               @Valid @RequestBody DiagnosisRequestDTO requestDTO) {
        DiagnosisResponseDTO diagnosis = diagnosisService.updateDiagnosis(recordId, diagnosisId, requestDTO);
        return ResponseEntity.ok().body(diagnosis);
    }

    @DeleteMapping("/{recordId}/diagnoses/{diagnosisId}")
    public ResponseEntity<Void> deleteDiagnosis(@PathVariable UUID recordId, @PathVariable UUID diagnosisId) {
        diagnosisService.deleteDiagnosis(recordId, diagnosisId);
        return ResponseEntity.ok().build();
    }
}
