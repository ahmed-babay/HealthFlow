package com.appointment.service.appointmentservice.repository;

import com.appointment.service.appointmentservice.model.Appointment;
import com.appointment.service.appointmentservice.model.Appointment.AppointmentStatus;
import com.appointment.service.appointmentservice.model.Appointment.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find appointments by patient ID
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);

    // Find appointments by doctor ID
    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeDesc(Long doctorId);

    // Find appointments by status
    List<Appointment> findByStatusOrderByAppointmentDateTimeAsc(AppointmentStatus status);

    // Find appointments by type
    List<Appointment> findByTypeOrderByAppointmentDateTimeDesc(AppointmentType type);

    // Find appointments by patient ID and status
    List<Appointment> findByPatientIdAndStatusOrderByAppointmentDateTimeDesc(Long patientId, AppointmentStatus status);

    // Find appointments by doctor ID and status
    List<Appointment> findByDoctorIdAndStatusOrderByAppointmentDateTimeDesc(Long doctorId, AppointmentStatus status);

    // Find appointments by date range
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :startDate AND :endDate ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByAppointmentDateTimeBetween(@Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate);

    // Find appointments by doctor and date range
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDateTime BETWEEN :startDate AND :endDate ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(@Param("doctorId") Long doctorId,
                                                                 @Param("startDate") LocalDateTime startDate,
                                                                 @Param("endDate") LocalDateTime endDate);

    // Find appointments by patient and date range
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.appointmentDateTime BETWEEN :startDate AND :endDate ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(@Param("patientId") Long patientId,
                                                                  @Param("startDate") LocalDateTime startDate,
                                                                  @Param("endDate") LocalDateTime endDate);

    // Find upcoming appointments (future appointments)
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime > :currentTime ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointments(@Param("currentTime") LocalDateTime currentTime);

    // Find upcoming appointments for a specific doctor
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDateTime > :currentTime ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctorId") Long doctorId, 
                                                      @Param("currentTime") LocalDateTime currentTime);

    // Find upcoming appointments for a specific patient
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.appointmentDateTime > :currentTime ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId, 
                                                       @Param("currentTime") LocalDateTime currentTime);

    // Find appointments by doctor, date, and time (for conflict checking)
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDateTime = :appointmentDateTime")
    Optional<Appointment> findByDoctorIdAndAppointmentDateTime(@Param("doctorId") Long doctorId, 
                                                              @Param("appointmentDateTime") LocalDateTime appointmentDateTime);

    // Find appointments by doctor and date (for daily schedule)
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND DATE(a.appointmentDateTime) = DATE(:appointmentDate) ORDER BY a.appointmentDateTime ASC")
    List<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId, 
                                           @Param("appointmentDate") LocalDateTime appointmentDate);

    // Count appointments by status
    long countByStatus(AppointmentStatus status);

    // Count appointments by doctor and status
    long countByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // Count appointments by patient and status
    long countByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    // Find appointments with notes (for follow-up tracking)
    @Query("SELECT a FROM Appointment a WHERE a.notes IS NOT NULL AND a.notes != '' ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findAppointmentsWithNotes();

    // Find appointments by doctor and notes
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.notes IS NOT NULL AND a.notes != '' ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findByDoctorIdAndNotesNotNull(@Param("doctorId") Long doctorId);

    // Find appointments by patient and notes
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.notes IS NOT NULL AND a.notes != '' ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findByPatientIdAndNotesNotNull(@Param("patientId") Long patientId);
}
