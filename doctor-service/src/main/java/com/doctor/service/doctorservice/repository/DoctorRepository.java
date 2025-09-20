package com.doctor.service.doctorservice.repository;

import com.doctor.service.doctorservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    // Email uniqueness checks
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);

    // License number uniqueness checks
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByLicenseNumberAndIdNot(String licenseNumber, UUID id);

    // Find by unique fields
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    // Search by specialization (case-insensitive)
    @Query("SELECT d FROM Doctor d WHERE LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))")
    List<Doctor> findBySpecializationContainingIgnoreCase(@Param("specialization") String specialization);

    // Find doctors by experience level
    List<Doctor> findByYearsOfExperienceGreaterThanEqual(Integer minYears);

    // Find senior doctors (10+ years experience)
    @Query("SELECT d FROM Doctor d WHERE d.yearsOfExperience >= 10 ORDER BY d.yearsOfExperience DESC")
    List<Doctor> findSeniorDoctors();
}
