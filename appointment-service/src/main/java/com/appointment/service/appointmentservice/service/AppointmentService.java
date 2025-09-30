package com.appointment.service.appointmentservice.service;

import com.appointment.service.appointmentservice.dto.AppointmentRequestDTO;
import com.appointment.service.appointmentservice.dto.AppointmentResponseDTO;
import com.appointment.service.appointmentservice.exception.AppointmentNotFoundException;
import com.appointment.service.appointmentservice.mapper.AppointmentMapper;
import com.appointment.service.appointmentservice.model.Appointment;
import com.appointment.service.appointmentservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    /**
     * Create a new appointment
     */
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {
        // Check for scheduling conflicts
        if (hasSchedulingConflict(requestDTO.getDoctorId(), requestDTO.getAppointmentDateTime())) {
            throw new IllegalArgumentException("Doctor has a conflicting appointment at this time");
        }

        Appointment appointment = appointmentMapper.toModel(requestDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    /**
     * Get appointment by ID
     */
    @Transactional(readOnly = true)
    public AppointmentResponseDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
        return appointmentMapper.toDTO(appointment);
    }

    /**
     * Get all appointments
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Update appointment
     */
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        // Check for scheduling conflicts if time is being changed
        if (requestDTO.getAppointmentDateTime() != null && 
            !requestDTO.getAppointmentDateTime().equals(existingAppointment.getAppointmentDateTime())) {
            if (hasSchedulingConflict(requestDTO.getDoctorId(), requestDTO.getAppointmentDateTime())) {
                throw new IllegalArgumentException("Doctor has a conflicting appointment at this time");
            }
        }

        appointmentMapper.updateModel(existingAppointment, requestDTO);
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    /**
     * Delete appointment
     */
    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }

    /**
     * Get appointments by patient ID
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get appointments by doctor ID
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeDesc(doctorId);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get appointments by status
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByStatus(Appointment.AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByStatusOrderByAppointmentDateTimeAsc(status);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get patient appointments by status
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getPatientAppointmentsByStatus(Long patientId, Appointment.AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndStatusOrderByAppointmentDateTimeDesc(patientId, status);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get doctor appointments by status
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getDoctorAppointmentsByStatus(Long doctorId, Appointment.AppointmentStatus status) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndStatusOrderByAppointmentDateTimeDesc(doctorId, status);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get upcoming appointments for a doctor
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getUpcomingAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByDoctor(doctorId, LocalDateTime.now());
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get upcoming appointments for a patient
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getUpcomingAppointmentsByPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPatient(patientId, LocalDateTime.now());
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get doctor's daily schedule
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getDoctorDailySchedule(Long doctorId, LocalDateTime date) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get appointment statistics
     */
    @Transactional(readOnly = true)
    public long getAppointmentCountByStatus(Appointment.AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }

    /**
     * Get doctor appointment statistics
     */
    @Transactional(readOnly = true)
    public long getDoctorAppointmentCountByStatus(Long doctorId, Appointment.AppointmentStatus status) {
        return appointmentRepository.countByDoctorIdAndStatus(doctorId, status);
    }

    /**
     * Check for scheduling conflicts
     */
    @Transactional(readOnly = true)
    public boolean hasSchedulingConflict(Long doctorId, LocalDateTime appointmentDateTime) {
        Optional<Appointment> conflictingAppointment = appointmentRepository
                .findByDoctorIdAndAppointmentDateTime(doctorId, appointmentDateTime);
        return conflictingAppointment.isPresent();
    }

    /**
     * Reschedule appointment
     */
    public AppointmentResponseDTO rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        // Check for conflicts at new time
        if (hasSchedulingConflict(existingAppointment.getDoctorId(), newDateTime)) {
            throw new IllegalArgumentException("Doctor has a conflicting appointment at the new time");
        }

        existingAppointment.setAppointmentDateTime(newDateTime);
        existingAppointment.setStatus(Appointment.AppointmentStatus.RESCHEDULED);
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    /**
     * Cancel appointment
     */
    public AppointmentResponseDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    /**
     * Complete appointment
     */
    public AppointmentResponseDTO completeAppointment(Long id, String notes) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        if (notes != null && !notes.trim().isEmpty()) {
            appointment.setNotes(notes);
        }
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    /**
     * Get appointments with notes (for follow-up tracking)
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsWithNotes() {
        List<Appointment> appointments = appointmentRepository.findAppointmentsWithNotes();
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get doctor appointments with notes
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getDoctorAppointmentsWithNotes(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndNotesNotNull(doctorId);
        return appointmentMapper.toDTOList(appointments);
    }

    /**
     * Get patient appointments with notes
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getPatientAppointmentsWithNotes(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndNotesNotNull(patientId);
        return appointmentMapper.toDTOList(appointments);
    }
}
