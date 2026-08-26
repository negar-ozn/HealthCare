package com.healthcare.healthcare_api.appointment;

import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.patient.PatientRepository;
import com.healthcare.healthcare_api.shared.exception.*;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final IdentifierGeneratorService generatorService;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, IdentifierGeneratorService generatorService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.generatorService = generatorService;
    }

    // By Patient
    public Appointment addAppointment(AppointmentRequest request) {

        appointmentRepository.findById(request.id())
                .orElseThrow(() -> new AlreadyMadeAppointmentException("The appointment has already made!"));
        Appointment appointment = Appointment.builder()
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .visitReason(request.visitReason())
                .patient(request.patient())
                .doctor(request.doctor())
                .appointmentNumber(generatorService.generatorAppointmentIdentifier())
//                .version()
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By Patient
    public Appointment rescheduledAppointment(Long id, AppointmentRequest request) {

        Appointment appointmentOp = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchAppointmentFoundException("No such appointment found!"));

        if (appointmentOp.getStatus().equals(AppointmentStatus.CONFIRMED))
            throw new RescheduledAppointmentException("After confirmation, appointments cannot be rescheduled.");

        suspendAppointment(id, request.status());
        Appointment newAppointment = Appointment.builder()
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .status(AppointmentStatus.SCHEDULED)
                .visitReason(request.visitReason())
                .rescheduled(true)
                .patient(request.patient())
                .doctor(request.doctor())
                .appointmentNumber(generatorService.generatorAppointmentIdentifier())
//                .version()
                .build();
        appointmentRepository.save(newAppointment);
        return newAppointment;
    }

    // By Patient
    public Appointment editAppointment(Long id, AppointmentRequest request) {

        Appointment appointmentOp = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchAppointmentFoundException("No such appointment found!"));

        if (request.status().equals(AppointmentStatus.CONFIRMED))
            throw new AppointmentModificationIsUnavailableException("After confirmation appointments cannot be modified");
        Appointment appointment = Appointment.builder()
                .id(appointmentOp.getId())
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .status(appointmentOp.getStatus())
                .visitReason(request.visitReason())
                .cancellationReason(request.cancellationReason())
                .rescheduled(request.rescheduled())
                .appointmentNumber(request.appointmentNumber())
//                .version()
                .patient(appointmentOp.getPatient())
                .doctor(request.doctor())
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By Patient
    public AppointmentResponse getAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchAppointmentFoundException("No such appointment found!"));
        return AppointmentResponse.builder()
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .visitReason(appointment.getVisitReason())
                .cancellationReason(appointment.getCancellationReason())
                .rescheduled(appointment.getRescheduled())
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .appointmentNumber(appointment.getAppointmentNumber())
                .build();
    }

    // By Patient, Doctor, and Staff
    public List<AppointmentResponse> allAppointment(Doctor doctor, Patient patient, LocalDate appointmentDate, Long nationalId) {

        List<Appointment> appointmentList =
                appointmentRepository.findByDoctorOrPatientOrAppointmentDateOrPatient_NationalId(doctor, patient,
                        appointmentDate, nationalId);
        if (appointmentList.isEmpty())
            throw new NoSuchPersonnelFoundException("No such record found!");

        return appointmentList.stream().map(appointment -> AppointmentResponse.builder()
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .visitReason(appointment.getVisitReason())
                .cancellationReason(appointment.getCancellationReason())
                .rescheduled(appointment.getRescheduled())
                .appointmentNumber(appointment.getAppointmentNumber())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .build()).toList();
    }

    // By Patient
    public void cancelAppointment(Long id, AppointmentStatus status) {

        Optional<Appointment> appointmentOp = appointmentRepository.findById(id);
        if (appointmentOp.isPresent() && status.equals(AppointmentStatus.SCHEDULED)) {
            Appointment appointment = appointmentOp.get();
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        }
    }

    // By Admin only!
    public void deleteAppointment(Long id) {

        Optional<Appointment> appointmentOp = appointmentRepository.findById(id);
        appointmentOp.ifPresent(appointment -> appointmentRepository.deleteById(appointment.getId()));
    }

    // By Patient
    public void confirmAppointment(Long id, AppointmentStatus status) {

        Optional<Appointment> appointmentOp = appointmentRepository.findById(id);
        if (appointmentOp.isPresent() && status.equals(AppointmentStatus.SCHEDULED)) {
            Appointment appointment = appointmentOp.get();
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointmentRepository.save(appointment);
        }
    }

    // By Staff
    public Appointment checkInAppointment(Long nationalId) {

        Appointment appointmentOp = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new NoSuchRecordFoundException("No scheduled appointment with this national id."));
        Appointment appointment = Appointment.builder()
                .id(appointmentOp.getId())
                .status(AppointmentStatus.CHECKED_IN)
                .build();
        appointmentRepository.save(appointment);

        Patient patientOp = patientRepository.findPatientByNationalId(nationalId)
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));
        Patient patient = Patient.builder()
                .id(patientOp.getId())
                .arrival(Boolean.TRUE)
                .build();
        patientRepository.save(patient);
        return appointment;
    }

    // By Staff
    public Appointment noShowAppointment(LocalDate appointmentDate, AppointmentStatus status) {

        Appointment appointmentOp = appointmentRepository.findByAppointmentDateAndStatus(appointmentDate, status)
                .orElseThrow(() -> new NoSuchRecordFoundException("No such record found"));
        Appointment appointment = Appointment.builder()
                .id(appointmentOp.getId())
                .status(AppointmentStatus.NO_SHOW)
                .build();
        appointmentRepository.save(appointment);

        Patient patientOp = patientRepository.findPatientByNationalId(appointment.getPatient().getNationalId())
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));
        Patient patient = Patient.builder()
                .id(patientOp.getId())
                .arrival(Boolean.FALSE)
                .build();
        patientRepository.save(patient);
        return appointment;
    }

    // By doctor
    public Appointment inProgressAppointment(Long nationalId) {

        Appointment appointmentOp = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new NoSuchRecordFoundException("No scheduled appointment with this national id."));
        Appointment appointment = Appointment.builder()
                .id(appointmentOp.getId())
                .status(AppointmentStatus.IN_PROGRESS)
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    // By doctor, when prescription has submitted this method will be called!
    public Appointment completedAppointment(Long nationalId) {

        Appointment appointmentOp = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new NoSuchRecordFoundException("No scheduled appointment with this national id."));
        Appointment appointment = Appointment.builder()
                .id(appointmentOp.getId())
                .status(AppointmentStatus.COMPLETED)
                .build();
        appointmentRepository.save(appointment);
        return appointment;
    }

    // run by the time of system! main page for staff
    public Page<AppointmentResponse> todayAppointments(LocalDate todayAppointmentDate, Pageable pageable) {

        Page<Appointment> todayAppointments = appointmentRepository.findAllByTodayAppointments(todayAppointmentDate, pageable);
        if (todayAppointments.isEmpty()) {
            throw new NoBookedAppointmentFoundException("No booked appointment for today!");
        }
        return todayAppointments.map(appointment -> AppointmentResponse.builder()
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .visitReason(appointment.getVisitReason())
                .cancellationReason(appointment.getCancellationReason())
                .rescheduled(appointment.getRescheduled())
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .appointmentNumber(appointment.getAppointmentNumber())
                .build());
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
}
