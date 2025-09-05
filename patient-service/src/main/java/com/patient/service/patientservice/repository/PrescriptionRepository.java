package com.patient.service.patientservice.repository;

import com.patient.service.patientservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    // Find all prescriptions for a specific medical record
    List<Prescription> findByMedicalRecordIdOrderByPrescribedDateDesc(UUID medicalRecordId);

    // Find active prescriptions for a medical record
    List<Prescription> findByMedicalRecordIdAndStatusOrderByPrescribedDateDesc(UUID medicalRecordId, Prescription.PrescriptionStatus status);

    // Count prescriptions for a medical record
    long countByMedicalRecordId(UUID medicalRecordId);

    // Find prescriptions by medication name
    List<Prescription> findByMedicationNameContainingIgnoreCase(String medicationName);

    // Find prescriptions by prescribing doctor
    List<Prescription> findByPrescribingDoctorOrderByPrescribedDateDesc(String prescribingDoctor);

    // Find expiring prescriptions (end date within specified days)
    @Query("SELECT p FROM Prescription p WHERE p.endDate BETWEEN :startDate AND :endDate " +
           "AND p.status = :status ORDER BY p.endDate ASC")
    List<Prescription> findExpiringPrescriptions(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") Prescription.PrescriptionStatus status);
}
