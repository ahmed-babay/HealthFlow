package com.patient.service.patientservice.repository;

import com.patient.service.patientservice.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {

    // Find all diagnoses for a specific medical record
    List<Diagnosis> findByMedicalRecordIdOrderByDiagnosedDateDesc(UUID medicalRecordId);

    // Find active diagnoses for a medical record
    List<Diagnosis> findByMedicalRecordIdAndStatusOrderByDiagnosedDateDesc(UUID medicalRecordId, Diagnosis.DiagnosisStatus status);

    // Count diagnoses for a medical record
    long countByMedicalRecordId(UUID medicalRecordId);

    // Find diagnoses by diagnosis code
    List<Diagnosis> findByDiagnosisCodeContainingIgnoreCase(String diagnosisCode);
}
