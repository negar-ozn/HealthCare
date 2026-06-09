package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.AppointmentDto;
import com.healthcare.healthcare.entitiy.Appointment;
import com.healthcare.healthcare.entitiy.Doctor;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.enums.AppointmentStatus;
import com.healthcare.healthcare.exception.*;
import com.healthcare.healthcare.repository.AppointmentRepository;
import com.healthcare.healthcare.repository.PatientRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    // By Patient
    public Appointment addAppointment(AppointmentDto appointmentDto) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentDto.getId());
        if (appointmentOpt.isPresent())
            try {
                throw new AlreadyMadeAppointment("The appointment has already made!");
            } catch (AlreadyMadeAppointment e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = new Appointment();
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setDoctor(appointmentDto.getDoctor());
        appointment.setPatient(appointmentDto.getPatient());
        appointment.setVisitReason(appointmentDto.getVisitReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By Patient
    public Appointment rescheduledAppointment(Long id, AppointmentDto appointmentDto) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchAppointmentFound("No such appointment found!");
            } catch (NoSuchAppointmentFound e) {
                throw new RuntimeException(e);
            }
        Appointment currentAppointment = appointmentOpt.get();
        if (currentAppointment.getStatus().equals(AppointmentStatus.CONFIRMED))
            try {
                throw new RescheduledAppointmentException("After confirmation appointments cannot be rescheduled.");
            } catch (RescheduledAppointmentException e) {
                throw new RuntimeException(e);
            }
        suspendAppointment(id, currentAppointment.getStatus());
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(appointmentDto.getPatient());
        newAppointment.setDoctor(appointmentDto.getDoctor());
        newAppointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        newAppointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        newAppointment.setVisitReason(appointmentDto.getVisitReason());
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(newAppointment);
        return newAppointment;
    }

    // By Patient
    public Appointment editAppointment(Long id, AppointmentDto appointmentDto) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchAppointmentFound("No such appointment found!");
            } catch (NoSuchAppointmentFound e) {
                throw new RuntimeException(e);
            }
        if (appointmentDto.getStatus().equals(AppointmentStatus.CONFIRMED))
            try {
                throw new AppointmentModificationIsUnavailableException("After confirmation appointments cannot be modified");
            } catch (AppointmentModificationIsUnavailableException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        appointment.setId(id);
        appointment.setAppointmentTime(appointmentDto.getAppointmentTime());
        appointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        appointment.setVisitReason(appointmentDto.getVisitReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By Patient
    public AppointmentDto getAppointment(Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchAppointmentFound("No such appointment found!");
            } catch (NoSuchAppointmentFound e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(id);
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setVisitReason(appointment.getVisitReason());
        appointmentDto.setRescheduled(appointment.getRescheduled());
        appointmentDto.setCancellationReason(appointment.getCancellationReason());
        appointmentDto.setDoctor(appointment.getDoctor());
        appointmentDto.setPatient(appointment.getPatient());
        appointmentDto.setStatus(appointment.getStatus());
        return appointmentDto;
    }

    // By Patient, Doctor, and Staff
    public AppointmentDto allAppointment(Doctor doctor, Patient patient, LocalDate appointmentDate,Long nationalId) {
        Optional<Appointment> appointmentOpt =
                appointmentRepository.findByDoctorOrPatientOrAppointmentDateOrPatient_NationalId(doctor, patient, appointmentDate, nationalId);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchRecordFoundException("No such record found!");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        return getAppointmentDto(appointment);
    }

    private static @NonNull AppointmentDto getAppointmentDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(appointment.getId());
        appointmentDto.setDoctor(appointment.getDoctor());
        appointmentDto.setVisitReason(appointment.getVisitReason());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDto.setRescheduled(appointment.getRescheduled());
        appointmentDto.setCancellationReason(appointment.getCancellationReason());
        appointmentDto.setPatient(appointment.getPatient());
        appointmentDto.setStatus(appointment.getStatus());
        return appointmentDto;
    }

    // By Patient
    public void cancelAppointment(Long id, AppointmentStatus status) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent() && status.equals(AppointmentStatus.SCHEDULED)) {
            Appointment appointment = appointmentOpt.get();
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        }
    }

    // By Admin only!
    public void deleteAppointment(Long id) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        appointmentOpt.ifPresent(appointment -> appointmentRepository.deleteById(appointment.getId()));
    }

    // By Patient
    public void confirmAppointment(Long id, AppointmentStatus status) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent() && status.equals(AppointmentStatus.SCHEDULED)) {
            Appointment appointment = appointmentOpt.get();
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
        }
    }

    // By Staff
    public Appointment checkInAppointment(Long nationalId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByPatient_NationalId(nationalId);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchRecordFoundException("No scheduled appointment with this national id.");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        appointment.setId(appointment.getId());
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        appointmentRepository.save(appointment);

        Optional<Patient> optionalPatient = patientRepository.findPatientByNationalId(nationalId);
        if (optionalPatient.isEmpty())
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        Patient patient = optionalPatient.get();
        patient.setId(patient.getId());
        patient.setArrival(Boolean.TRUE);
        patientRepository.save(patient);

        return appointment;
    }

    // By Staff
    public Appointment noShowAppointment(LocalDate appointmentDate) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findByAppointmentDate(appointmentDate);
        if (optionalAppointment.isEmpty())
            try {
                throw new NoSuchRecordFoundException("No such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = optionalAppointment.get();
        if (appointment.getAppointmentDate().equals(LocalDate.now()) && appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
            appointment.setId(appointment.getId());
            appointment.setStatus(AppointmentStatus.NO_SHOW);
            appointmentRepository.save(appointment);

            Optional<Patient> optionalPatient = patientRepository.findPatientByNationalId(appointment.getPatient().getNationalId());
            if (optionalPatient.isEmpty())
                try {
                    throw new NoSuchPatientFoundException("No such patient found");
                } catch (NoSuchPatientFoundException e) {
                    throw new RuntimeException(e);
                }
            Patient patient = optionalPatient.get();
            patient.setId(patient.getId());
            patient.setArrival(Boolean.FALSE);
            patientRepository.save(patient);
        }
        return appointment;
    }

    // By doctor
    public Appointment inProgressAppointment(Long nationalId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByPatient_NationalId(nationalId);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchRecordFoundException("No scheduled appointment with this national id.");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        appointment.setId(appointment.getId());
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By doctor, when prescription has submitted this method will be called!
    public Appointment completedAppointment(Long nationalId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findByPatient_NationalId(nationalId);
        if (appointmentOpt.isEmpty())
            try {
                throw new NoSuchRecordFoundException("No scheduled appointment with this national id.");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        Appointment appointment = appointmentOpt.get();
        appointment.setId(appointment.getId());
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return appointment;
    }

    // run by the time of system! main page for staff
    public List<AppointmentDto> todayAppointments(LocalDate todayAppointmentDate) {
        List<Appointment> todayAppointmentsList = appointmentRepository.findAllByAppointmentDate(todayAppointmentDate);
        List<AppointmentDto> appointmentsList = new ArrayList<>();
        for (Appointment appointment : todayAppointmentsList) {
            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
            appointmentDto.setAppointmentDate(appointment.getAppointmentDate());
            appointmentDto.setPatient(appointment.getPatient());
            appointmentDto.setDoctor(appointment.getDoctor());
            appointmentDto.setStatus(appointment.getStatus());
            appointmentDto.setRescheduled(appointment.getRescheduled());
            appointmentsList.add(appointmentDto);
        }
        return appointmentsList;
    }


    private void suspendAppointment(Long id, AppointmentStatus status) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
        if (appointmentOpt.isPresent() && status.equals(AppointmentStatus.SCHEDULED)) {
            Appointment appointment = appointmentOpt.get();
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.SUSPENDED);
            appointment.setRescheduled(Boolean.TRUE);
            appointmentRepository.save(appointment);
        }

    }

    //develop it later!
//    private Appointment checkAppointment(Long id) {
//        Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
//        if (appointmentOpt.isPresent())
//            throw new AlreadyMadeAppointment("The appointment has already made!");
//        else {
//            throw new NoSuchAppointmentFound("No such appointment found!");
//        }
//    }
}
