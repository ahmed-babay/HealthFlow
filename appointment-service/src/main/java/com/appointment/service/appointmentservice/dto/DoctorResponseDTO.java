package com.appointment.service.appointmentservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public class DoctorResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String specialization;
    private String licenseNumber;
    private String phoneNumber;
    private Integer yearsOfExperience;
    private LocalDate registrationDate;

    // Constructors
    public DoctorResponseDTO() {}

    public DoctorResponseDTO(UUID id, String name, String email, String specialization,
                            String licenseNumber, String phoneNumber, 
                            Integer yearsOfExperience, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.registrationDate = registrationDate;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "DoctorResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
