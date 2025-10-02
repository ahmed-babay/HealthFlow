 package com.appointment.service.appointmentservice.controller;

import com.appointment.service.appointmentservice.dto.AppointmentRequestDTO;
import com.appointment.service.appointmentservice.dto.AppointmentResponseDTO;
import com.appointment.service.appointmentservice.model.Appointment;
import com.appointment.service.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Health check endpoint (no authentication required)
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "appointment-service");
        response.put("timestamp", LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a new appointment
     */
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        try {
            AppointmentResponseDTO responseDTO = appointmentService.createAppointment(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * Get appointment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        try {
            AppointmentResponseDTO responseDTO = appointmentService.getAppointmentById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all appointments
     */
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Update appointment
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable Long id, 
                                                                   @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        try {
            AppointmentResponseDTO responseDTO = appointmentService.updateAppointment(id, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete appointment
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        try {
            appointmentService.deleteAppointment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get appointments by patient ID
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get appointments by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get appointments by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(@PathVariable Appointment.AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByStatus(status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get patient appointments by status
     */
    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointmentsByStatus(@PathVariable Long patientId, 
                                                                                       @PathVariable Appointment.AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointmentsByStatus(patientId, status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get doctor appointments by status
     */
    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointmentsByStatus(@PathVariable Long doctorId, 
                                                                                      @PathVariable Appointment.AppointmentStatus status) {
        List<AppointmentResponseDTO> appointments = appointmentService.getDoctorAppointmentsByStatus(doctorId, status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get upcoming appointments for a doctor
     */
    @GetMapping("/doctor/{doctorId}/upcoming")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getUpcomingAppointmentsByDoctor(doctorId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get upcoming appointments for a patient
     */
    @GetMapping("/patient/{patientId}/upcoming")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getUpcomingAppointmentsByPatient(patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get doctor's daily schedule
     */
    @GetMapping("/doctor/{doctorId}/schedule")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorDailySchedule(@PathVariable Long doctorId, 
                                                                              @RequestParam String date) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date + "T00:00:00");
            List<AppointmentResponseDTO> appointments = appointmentService.getDoctorDailySchedule(doctorId, dateTime);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get appointment statistics by status
     */
    @GetMapping("/stats/status/{status}")
    public ResponseEntity<Long> getAppointmentCountByStatus(@PathVariable Appointment.AppointmentStatus status) {
        long count = appointmentService.getAppointmentCountByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * Get doctor appointment statistics by status
     */
    @GetMapping("/stats/doctor/{doctorId}/status/{status}")
    public ResponseEntity<Long> getDoctorAppointmentCountByStatus(@PathVariable Long doctorId, 
                                                                 @PathVariable Appointment.AppointmentStatus status) {
        long count = appointmentService.getDoctorAppointmentCountByStatus(doctorId, status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    /**
     * Check for scheduling conflicts
     */
    @GetMapping("/conflict/doctor/{doctorId}")
    public ResponseEntity<Boolean> hasSchedulingConflict(@PathVariable Long doctorId, 
                                                        @RequestParam String dateTime) {
        try {
            LocalDateTime appointmentDateTime = LocalDateTime.parse(dateTime);
            boolean hasConflict = appointmentService.hasSchedulingConflict(doctorId, appointmentDateTime);
            return new ResponseEntity<>(hasConflict, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Reschedule appointment
     */
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(@PathVariable Long id, 
                                                                       @RequestParam String newDateTime) {
        try {
            LocalDateTime newDate = LocalDateTime.parse(newDateTime);
            AppointmentResponseDTO responseDTO = appointmentService.rescheduleAppointment(id, newDate);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Cancel appointment
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {
        try {
            AppointmentResponseDTO responseDTO = appointmentService.cancelAppointment(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Complete appointment
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponseDTO> completeAppointment(@PathVariable Long id, 
                                                                     @RequestParam(required = false) String notes) {
        try {
            AppointmentResponseDTO responseDTO = appointmentService.completeAppointment(id, notes);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get appointments with notes
     */
    @GetMapping("/with-notes")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsWithNotes() {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsWithNotes();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get doctor appointments with notes
     */
    @GetMapping("/doctor/{doctorId}/with-notes")
    public ResponseEntity<List<AppointmentResponseDTO>> getDoctorAppointmentsWithNotes(@PathVariable Long doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getDoctorAppointmentsWithNotes(doctorId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    /**
     * Get patient appointments with notes
     */
    @GetMapping("/patient/{patientId}/with-notes")
    public ResponseEntity<List<AppointmentResponseDTO>> getPatientAppointmentsWithNotes(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointmentsWithNotes(patientId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}

