package com.doctor.service.doctorservice.service;

import com.doctor.service.doctorservice.dto.DoctorRequestDTO;
import com.doctor.service.doctorservice.dto.DoctorResponseDTO;
import com.doctor.service.doctorservice.exception.DoctorNotFoundException;
import com.doctor.service.doctorservice.mapper.DoctorMapper;
import com.doctor.service.doctorservice.model.Doctor;
import com.doctor.service.doctorservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer for Doctor entity
 * Contains business logic and coordinates between controller and repository layers
 */
@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    /**
     * Creates a new doctor in the system
     * 
     * @param requestDTO doctor data from client request
     * @return created doctor as response DTO
     * @throws IllegalArgumentException if email or license number already exists
     */
    public DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO) {
        // Check if email already exists
        if (doctorRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Doctor with email " + requestDTO.getEmail() + " already exists");
        }

        // Check if license number already exists
        if (doctorRepository.existsByLicenseNumber(requestDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("Doctor with license number " + requestDTO.getLicenseNumber() + " already exists");
        }

        Doctor doctor = doctorMapper.toModel(requestDTO);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDTO(savedDoctor);
    }

    /**
     * Retrieves a doctor by ID
     * 
     * @param id doctor's unique identifier
     * @return doctor as response DTO
     * @throws DoctorNotFoundException if doctor not found
     */
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));
        return doctorMapper.toDTO(doctor);
    }

    /**
     * Retrieves all doctors in the system
     * 
     * @return list of all doctors as response DTOs
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing doctor
     * 
     * @param id doctor's unique identifier
     * @param requestDTO updated doctor data
     * @return updated doctor as response DTO
     * @throws DoctorNotFoundException if doctor not found
     * @throws IllegalArgumentException if email or license conflicts with another doctor
     */
    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO requestDTO) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + id));

        // Check if email conflicts with another doctor
        if (!existingDoctor.getEmail().equals(requestDTO.getEmail()) &&
            doctorRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Email " + requestDTO.getEmail() + " is already in use by another doctor");
        }

        // Check if license number conflicts with another doctor
        if (!existingDoctor.getLicenseNumber().equals(requestDTO.getLicenseNumber()) &&
            doctorRepository.existsByLicenseNumber(requestDTO.getLicenseNumber())) {
            throw new IllegalArgumentException("License number " + requestDTO.getLicenseNumber() + " is already in use by another doctor");
        }

        Doctor updatedDoctor = doctorMapper.updateModel(existingDoctor, requestDTO);
        Doctor savedDoctor = doctorRepository.save(updatedDoctor);
        return doctorMapper.toDTO(savedDoctor);
    }

    /**
     * Deletes a doctor from the system
     * 
     * @param id doctor's unique identifier
     * @throws DoctorNotFoundException if doctor not found
     */
    public void deleteDoctor(UUID id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    /**
     * Searches doctors by specialization (case-insensitive, partial match)
     * 
     * @param specialization specialization to search for
     * @return list of doctors matching the specialization
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization)
                .stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Searches doctors by years of experience (minimum threshold)
     * 
     * @param minYears minimum years of experience
     * @return list of doctors with at least the specified years of experience
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsByExperience(Integer minYears) {
        return doctorRepository.findByYearsOfExperienceGreaterThanEqual(minYears)
                .stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a doctor by email
     * 
     * @param email doctor's email address
     * @return doctor as response DTO
     * @throws DoctorNotFoundException if doctor not found
     */
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorByEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with email: " + email));
        return doctorMapper.toDTO(doctor);
    }

    /**
     * Retrieves a doctor by license number
     * 
     * @param licenseNumber doctor's medical license number
     * @return doctor as response DTO
     * @throws DoctorNotFoundException if doctor not found
     */
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorByLicenseNumber(String licenseNumber) {
        Doctor doctor = doctorRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with license number: " + licenseNumber));
        return doctorMapper.toDTO(doctor);
    }
}
