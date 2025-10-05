package com.appointment.service.appointmentservice.service;

import com.appointment.service.appointmentservice.dto.DoctorResponseDTO;
import com.appointment.service.appointmentservice.dto.PatientResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class ExternalServiceClient {

    private final RestClient restClient = RestClient.create();
    
    @Value("${patient.service.url}")
    private String patientServiceUrl;
    
    @Value("${doctor.service.url}")
    private String doctorServiceUrl;

    /**
     * Check if patient exists (blocking call)
     */
    public Boolean checkPatientExists(Long patientId) {
        try {
            String url = patientServiceUrl + "/patients/" + patientId;
            PatientResponseDTO patient = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(PatientResponseDTO.class);
            System.out.println("Patient validation successful: " + (patient != null ? patient.getName() : "null"));
            return patient != null;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                System.out.println("Patient validation failed for ID " + patientId + ": Patient not found");
                return false;
            } else {
                System.out.println("Patient validation failed for ID " + patientId + ": " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Patient validation failed for ID " + patientId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if doctor exists (blocking call)
     */
    public Boolean checkDoctorExists(Long doctorId) {
        try {
            String url = doctorServiceUrl + "/api/v1/doctors/" + doctorId;
            DoctorResponseDTO doctor = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(DoctorResponseDTO.class);
            System.out.println("Doctor validation successful: " + (doctor != null ? doctor.getName() : "null"));
            return doctor != null;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                System.out.println("Doctor validation failed for ID " + doctorId + ": Doctor not found");
                return false;
            } else {
                System.out.println("Doctor validation failed for ID " + doctorId + ": " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Doctor validation failed for ID " + doctorId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Get patient details by ID (blocking call)
     */
    public PatientResponseDTO getPatientById(Long patientId) {
        try {
            String url = patientServiceUrl + "/patients/" + patientId;
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(PatientResponseDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new IllegalArgumentException("Patient with ID " + patientId + " not found");
            } else {
                throw new RuntimeException("Error fetching patient: " + e.getMessage());
            }
        }
    }

    /**
     * Get doctor details by ID (blocking call)
     */
    public DoctorResponseDTO getDoctorById(Long doctorId) {
        try {
            String url = doctorServiceUrl + "/api/v1/doctors/" + doctorId;
            return restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(DoctorResponseDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                throw new IllegalArgumentException("Doctor with ID " + doctorId + " not found");
            } else {
                throw new RuntimeException("Error fetching doctor: " + e.getMessage());
            }
        }
    }
}