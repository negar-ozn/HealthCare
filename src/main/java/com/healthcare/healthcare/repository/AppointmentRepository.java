package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.entitiy.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = "SELECT A FROM APPOINTMENT A WHERE A.doctor=:doctor OR A.patient=:patient OR " +
            "A.appointmentDate=:appointmentDate OR A.patient.nationalId=:nationalId")
    Optional<Appointment> findByDoctorOrPatientOrAppointmentDateOrPatient_NationalId(Doctor doctor, Patient patient,
                                                                                     LocalDate appointmentDate, Long nationalId);

    @Query(value = "SELECT A FROM APPOINTMENT A WHERE A.patient.nationalId=:nationalId")
    Optional<Appointment> findByPatient_NationalId(Long nationalId);

    @Query(value = "SELECT A FROM APPOINTMENT A WHERE A.appointmentDate=:appointmentDate")
    List<Appointment> findAllByAppointmentDate(LocalDate appointmentDate);

    @Query(value = "SELECT A FROM APPOINTMENT A WHERE A.appointmentDate=:appointmentDate")
    Optional<Appointment> findByAppointmentDate(LocalDate appointmentDate);
}
