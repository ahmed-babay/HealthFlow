package com.doctor.service.doctorservice.controller;

import com.doctor.service.doctorservice.dto.DoctorRequestDTO;
import com.doctor.service.doctorservice.dto.DoctorResponseDTO;
import com.doctor.service.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Doctor entity
 * Handles HTTP requests and responses for doctor-related operations
 */
@RestController
@RequestMapping("/api/v1/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Creates a new doctor
     * POST /api/v1/doctors
     * 
     * @param requestDTO doctor data from request body
     * @return created doctor with 201 status
     */
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO requestDTO) {
        DoctorResponseDTO createdDoctor = doctorService.createDoctor(requestDTO);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    /**
     * Retrieves a doctor by ID
     * GET /api/v1/doctors/{id}
     * 
     * @param id doctor's unique identifier
     * @return doctor data with 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    /**
     * Retrieves all doctors
     * GET /api/v1/doctors
     * 
     * @return list of all doctors with 200 status
     */
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * Updates an existing doctor
     * PUT /api/v1/doctors/{id}
     * 
     * @param id doctor's unique identifier
     * @param requestDTO updated doctor data
     * @return updated doctor with 200 status
     */
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorRequestDTO requestDTO) {
        DoctorResponseDTO updatedDoctor = doctorService.updateDoctor(id, requestDTO);
        return ResponseEntity.ok(updatedDoctor);
    }

    /**
     * Deletes a doctor
     * DELETE /api/v1/doctors/{id}
     * 
     * @param id doctor's unique identifier
     * @return 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches doctors by specialization
     * GET /api/v1/doctors/search/specialization?q={specialization}
     * 
     * @param specialization specialization to search for (partial match, case-insensitive)
     * @return list of doctors matching the specialization
     */
    @GetMapping("/search/specialization")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(
            @RequestParam("q") String specialization) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Searches doctors by minimum years of experience
     * GET /api/v1/doctors/search/experience?min={years}
     * 
     * @param minYears minimum years of experience
     * @return list of doctors with at least the specified experience
     */
    @GetMapping("/search/experience")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsByExperience(
            @RequestParam("min") Integer minYears) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsByExperience(minYears);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Retrieves a doctor by email
     * GET /api/v1/doctors/search/email?email={email}
     * 
     * @param email doctor's email address
     * @return doctor data with 200 status
     */
    @GetMapping("/search/email")
    public ResponseEntity<DoctorResponseDTO> getDoctorByEmail(@RequestParam String email) {
        DoctorResponseDTO doctor = doctorService.getDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    /**
     * Retrieves a doctor by license number
     * GET /api/v1/doctors/search/license?number={licenseNumber}
     * 
     * @param licenseNumber doctor's medical license number
     * @return doctor data with 200 status
     */
    @GetMapping("/search/license")
    public ResponseEntity<DoctorResponseDTO> getDoctorByLicenseNumber(
            @RequestParam("number") String licenseNumber) {
        DoctorResponseDTO doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
        return ResponseEntity.ok(doctor);
    }
}
