package com.appointment.service.appointmentservice.mapper;

import com.appointment.service.appointmentservice.dto.AppointmentRequestDTO;
import com.appointment.service.appointmentservice.dto.AppointmentResponseDTO;
import com.appointment.service.appointmentservice.model.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    /**
     * Convert AppointmentRequestDTO to Appointment entity
     */
    public Appointment toModel(AppointmentRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setPatientId(requestDTO.getPatientId());
        appointment.setDoctorId(requestDTO.getDoctorId());
        appointment.setAppointmentDateTime(requestDTO.getAppointmentDateTime());
        appointment.setDurationMinutes(requestDTO.getDurationMinutes());
        appointment.setAppointmentType(requestDTO.getType());
        
        // Set default status if not provided
        if (requestDTO.getStatus() != null) {
            appointment.setStatus(requestDTO.getStatus());
        } else {
            appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        }
        
        appointment.setNotes(requestDTO.getNotes());
        
        return appointment;
    }

    /**
     * Convert Appointment entity to AppointmentResponseDTO
     */
    public AppointmentResponseDTO toDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(appointment.getId());
        responseDTO.setPatientId(appointment.getPatientId());
        responseDTO.setDoctorId(appointment.getDoctorId());
        responseDTO.setAppointmentDateTime(appointment.getAppointmentDateTime());
        responseDTO.setDurationMinutes(appointment.getDurationMinutes());
        responseDTO.setType(appointment.getAppointmentType());
        responseDTO.setStatus(appointment.getStatus());
        responseDTO.setNotes(appointment.getNotes());
        responseDTO.setCreatedAt(appointment.getCreatedAt());
        responseDTO.setUpdatedAt(appointment.getUpdatedAt());
        
        return responseDTO;
    }

    /**
     * Update existing Appointment entity with data from AppointmentRequestDTO
     */
    public void updateModel(Appointment existingAppointment, AppointmentRequestDTO requestDTO) {
        if (existingAppointment == null || requestDTO == null) {
            return;
        }

        // Update only non-null fields from request DTO
        if (requestDTO.getPatientId() != null) {
            existingAppointment.setPatientId(requestDTO.getPatientId());
        }
        if (requestDTO.getDoctorId() != null) {
            existingAppointment.setDoctorId(requestDTO.getDoctorId());
        }
        if (requestDTO.getAppointmentDateTime() != null) {
            existingAppointment.setAppointmentDateTime(requestDTO.getAppointmentDateTime());
        }
        if (requestDTO.getDurationMinutes() != null) {
            existingAppointment.setDurationMinutes(requestDTO.getDurationMinutes());
        }
        if (requestDTO.getType() != null) {
            existingAppointment.setAppointmentType(requestDTO.getType());
        }
        if (requestDTO.getStatus() != null) {
            existingAppointment.setStatus(requestDTO.getStatus());
        }
        if (requestDTO.getNotes() != null) {
            existingAppointment.setNotes(requestDTO.getNotes());
        }
    }

    /**
     * Convert list of Appointment entities to list of AppointmentResponseDTOs
     */
    public List<AppointmentResponseDTO> toDTOList(List<Appointment> appointments) {
        if (appointments == null) {
            return null;
        }

        return appointments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of AppointmentRequestDTOs to list of Appointment entities
     */
    public List<Appointment> toModelList(List<AppointmentRequestDTO> requestDTOs) {
        if (requestDTOs == null) {
            return null;
        }

        return requestDTOs.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Create a copy of Appointment entity (useful for creating new appointments based on existing ones)
     */
    public Appointment copyModel(Appointment original) {
        if (original == null) {
            return null;
        }

        Appointment copy = new Appointment();
        copy.setPatientId(original.getPatientId());
        copy.setDoctorId(original.getDoctorId());
        copy.setAppointmentDateTime(original.getAppointmentDateTime());
        copy.setDurationMinutes(original.getDurationMinutes());
        copy.setAppointmentType(original.getAppointmentType());
        copy.setStatus(Appointment.AppointmentStatus.SCHEDULED); // Reset status for new appointment
        copy.setNotes(original.getNotes());
        
        return copy;
    }

    /**
     * Create a summary DTO with only essential fields (useful for list views)
     */
    public AppointmentResponseDTO toSummaryDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentResponseDTO summaryDTO = new AppointmentResponseDTO();
        summaryDTO.setId(appointment.getId());
        summaryDTO.setPatientId(appointment.getPatientId());
        summaryDTO.setDoctorId(appointment.getDoctorId());
        summaryDTO.setAppointmentDateTime(appointment.getAppointmentDateTime());
        summaryDTO.setDurationMinutes(appointment.getDurationMinutes());
        summaryDTO.setType(appointment.getAppointmentType());
        summaryDTO.setStatus(appointment.getStatus());
        // Exclude notes, createdAt, updatedAt for summary view
        
        return summaryDTO;
    }

    /**
     * Convert list of appointments to summary DTOs
     */
    public List<AppointmentResponseDTO> toSummaryDTOList(List<Appointment> appointments) {
        if (appointments == null) {
            return null;
        }

        return appointments.stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }
}
