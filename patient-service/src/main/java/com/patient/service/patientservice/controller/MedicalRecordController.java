package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.dto.MedicalRecordRequestDTO;
import com.patient.service.patientservice.dto.MedicalRecordResponseDTO;
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

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
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
}
