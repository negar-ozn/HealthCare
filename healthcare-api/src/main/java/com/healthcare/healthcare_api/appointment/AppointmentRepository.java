package com.healthcare.healthcare_api.appointment;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = """
            SELECT A
            FROM Appointment A
            WHERE A.doctor=:doctor
                OR A.patient=:patient
                OR A.appointmentDate=:appointmentDate
                OR A.patient.nationalId=:nationalId
            """)
    List<Appointment> findByDoctorOrPatientOrAppointmentDateOrPatient_NationalId(Doctor doctor, Patient patient,
                                                                                 LocalDate appointmentDate, Long nationalId);

    @Query(value = """
            SELECT A
            FROM Appointment A
            WHERE A.patient.nationalId=:nationalId
            """)
    Optional<Appointment> findByPatient_NationalId(Long nationalId);

    @Query(value = "SELECT A FROM Appointment A WHERE A.appointmentDate=:today")
    Page<Appointment> findAllByTodayAppointments(LocalDate today, Pageable pageable);

    @Query(value = "SELECT A FROM Appointment A WHERE A.appointmentDate=:appointmentDate AND A.status=:status")
    Optional<Appointment> findByAppointmentDateAndStatus(LocalDate appointmentDate, AppointmentStatus status);

    Optional<Appointment> findByAppointmentDateAndPatient_nationalId(LocalDate appointmentDate, Long patientNationalId);

    Appointment findBySchedule_Id(Long scheduleId);
}
