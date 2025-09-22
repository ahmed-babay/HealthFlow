package com.doctor.service.doctorservice.mapper;

import com.doctor.service.doctorservice.dto.DoctorRequestDTO;
import com.doctor.service.doctorservice.dto.DoctorResponseDTO;
import com.doctor.service.doctorservice.model.Doctor;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Doctor entity and DTOs
 * Handles the conversion logic between different layers of the application
 */
@Component
public class DoctorMapper {

    /**
     * Converts DoctorRequestDTO to Doctor entity
     * Used when creating or updating a doctor from client request
     * 
     * @param requestDTO the request DTO containing doctor data
     * @return Doctor entity ready for database operations
     */
    public Doctor toModel(DoctorRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Doctor doctor = new Doctor();
        doctor.setName(requestDTO.getName());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setSpecialization(requestDTO.getSpecialization());
        doctor.setLicenseNumber(requestDTO.getLicenseNumber());
        doctor.setPhoneNumber(requestDTO.getPhoneNumber());
        doctor.setYearsOfExperience(requestDTO.getYearsOfExperience());
        doctor.setRegisteredDate(requestDTO.getRegistrationDate());

        return doctor;
    }

    /**
     * Converts Doctor entity to DoctorResponseDTO
     * Used when returning doctor data to client
     * 
     * @param doctor the doctor entity from database
     * @return DoctorResponseDTO containing doctor data for response
     */
    public DoctorResponseDTO toDTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        DoctorResponseDTO responseDTO = new DoctorResponseDTO();
        responseDTO.setId(doctor.getId());
        responseDTO.setName(doctor.getName());
        responseDTO.setEmail(doctor.getEmail());
        responseDTO.setSpecialization(doctor.getSpecialization());
        responseDTO.setLicenseNumber(doctor.getLicenseNumber());
        responseDTO.setPhoneNumber(doctor.getPhoneNumber());
        responseDTO.setYearsOfExperience(doctor.getYearsOfExperience());
        responseDTO.setRegistrationDate(doctor.getRegisteredDate());

        return responseDTO;
    }

    /**
     * Updates an existing Doctor entity with data from DoctorRequestDTO
     * Used in update operations to preserve the ID and registeredDate
     * 
     * @param existingDoctor the existing doctor entity from database
     * @param requestDTO the request DTO containing updated data
     * @return updated Doctor entity
     */
    public Doctor updateModel(Doctor existingDoctor, DoctorRequestDTO requestDTO) {
        if (existingDoctor == null || requestDTO == null) {
            return existingDoctor;
        }

        existingDoctor.setName(requestDTO.getName());
        existingDoctor.setEmail(requestDTO.getEmail());
        existingDoctor.setSpecialization(requestDTO.getSpecialization());
        existingDoctor.setLicenseNumber(requestDTO.getLicenseNumber());
        existingDoctor.setPhoneNumber(requestDTO.getPhoneNumber());
        existingDoctor.setYearsOfExperience(requestDTO.getYearsOfExperience());
        
        return existingDoctor;
    }
}
