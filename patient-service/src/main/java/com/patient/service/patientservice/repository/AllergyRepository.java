package com.patient.service.patientservice.repository;

import com.patient.service.patientservice.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, UUID> {

    // Find all allergies for a specific patient
    List<Allergy> findByPatientIdOrderByDiagnosedDateDesc(UUID patientId);

    // Find only active allergies for a patient
    List<Allergy> findByPatientIdAndIsActiveOrderByDiagnosedDateDesc(UUID patientId, Boolean isActive);

    // Find allergies by type for a patient
    List<Allergy> findByPatientIdAndAllergyTypeOrderByDiagnosedDateDesc(UUID patientId, Allergy.AllergyType allergyType);

    // Find allergies by severity for a patient
    List<Allergy> findByPatientIdAndSeverityOrderByDiagnosedDateDesc(UUID patientId, Allergy.AllergySeverity severity);

    // Count allergies for a patient
    long countByPatientId(UUID patientId);

    // Count active allergies for a patient
    long countByPatientIdAndIsActive(UUID patientId, Boolean isActive);

    // Find allergies by allergen name (case-insensitive search)
    @Query("SELECT a FROM Allergy a WHERE a.patient.id = :patientId " +
           "AND LOWER(a.allergen) LIKE LOWER(CONCAT('%', :allergen, '%')) " +
           "ORDER BY a.diagnosedDate DESC")
    List<Allergy> findByPatientIdAndAllergenContaining(@Param("patientId") UUID patientId, 
                                                       @Param("allergen") String allergen);

    // Find severe allergies for critical alerts
    @Query("SELECT a FROM Allergy a WHERE a.patient.id = :patientId " +
           "AND a.severity IN (com.patient.service.patientservice.model.Allergy.AllergySeverity.SEVERE, " +
           "com.patient.service.patientservice.model.Allergy.AllergySeverity.LIFE_THREATENING) " +
           "AND a.isActive = true ORDER BY a.diagnosedDate DESC")
    List<Allergy> findSevereActiveAllergies(@Param("patientId") UUID patientId);
}
