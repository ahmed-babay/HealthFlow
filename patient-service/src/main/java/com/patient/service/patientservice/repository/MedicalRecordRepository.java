package com.patient.service.patientservice.repository;

import com.patient.service.patientservice.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {

    // Find all medical records for a specific patient
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(UUID patientId);

    // Find medical records by attending doctor
    List<MedicalRecord> findByAttendingDoctorOrderByRecordDateDesc(String attendingDoctor);

    // Find medical records within a date range for a patient
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
           "AND mr.recordDate BETWEEN :startDate AND :endDate " +
           "ORDER BY mr.recordDate DESC")
    List<MedicalRecord> findByPatientIdAndDateRange(
            @Param("patientId") UUID patientId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Count medical records for a patient
    long countByPatientId(UUID patientId);

    // Find the most recent medical record for a patient
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId " +
           "ORDER BY mr.recordDate DESC LIMIT 1")
    MedicalRecord findMostRecentByPatientId(@Param("patientId") UUID patientId);
}
