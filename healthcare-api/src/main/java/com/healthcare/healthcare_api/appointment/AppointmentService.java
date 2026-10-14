package com.healthcare.healthcare_api.appointment;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.doctor.Doctor;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.schedule.Schedule;
import com.healthcare.healthcare_api.schedule.ScheduleRepository;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.AppointmentStatus;
import com.healthcare.healthcare_api.patient.PatientRepository;
import com.healthcare.healthcare_api.shared.enums.ScheduleStatus;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.*;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ScheduleRepository scheduleRepository;
    private final PatientRepository patientRepository;
    private final IdentifierGeneratorService generatorService;
    private final CommonKafkaProducer commonKafkaProducer;

    public AppointmentService(AppointmentRepository appointmentRepository, ScheduleRepository scheduleRepository, PatientRepository patientRepository,
                              IdentifierGeneratorService generatorService, CommonKafkaProducer commonKafkaProducer) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleRepository = scheduleRepository;
        this.patientRepository = patientRepository;
        this.generatorService = generatorService;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public AppointmentResponse addAppointment(AppointmentRequest request) {

        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new RecordNotFoundException("Schedule not found."));

        if (schedule.getStatus() != ScheduleStatus.AVAILABLE)
            throw new AppointmentSlotAlreadyBookedException("This schedule is not available.");

        Long patientId = SecurityUtils.currentUserId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        Appointment appointment = Appointment.builder()
                .appointmentDate(schedule.getDate())
                .appointmentTime(schedule.getStartTime())
                .patient(patient)
                .doctor(schedule.getDoctor())
                .schedule(schedule)
                .status(AppointmentStatus.SCHEDULED)
                .visitReason(request.visitReason())
                .appointmentNumber(generatorService.generatorAppointmentIdentifier())
                .build();

        try {
            Appointment savedAppointment = appointmentRepository.saveAndFlush(appointment);

            commonKafkaProducer.publishAppointmentBookedEvent(CoreEvent.builder()
                    .userId(SecurityUtils.currentUserId())
                    .email(SecurityUtils.currentUserEmail())
                    .username(SecurityUtils.currentUsername())
                    .action(ActionType.APPOINTMENT_CREATE.name())
                    .entityName(EntityName.APPOINTMENT)
                    .status(Status.SUCCESSFUL.name())
                    .details("Appointment " + savedAppointment.getAppointmentNumber() + " created successfully.")
                    .eventType(EventType.APPOINTMENT_BOOKED)
                    .build());

            return appointmentResponse(savedAppointment);
        } catch (DataIntegrityViolationException e) {
            throw new AppointmentSlotAlreadyBookedException("This appointment slot is no longer available.");
        }
    }

    @Transactional
    public AppointmentResponse rescheduledAppointment(Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No such appointment found!"));

        if (appointment.getStatus().equals(AppointmentStatus.CONFIRMED))
            throw new RescheduledAppointmentException("After confirmation, appointments cannot be rescheduled.");

        suspendAppointment(id, appointment.getStatus());

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setVisitReason(request.visitReason());
        appointment.setRescheduled(true);
        appointment.setAppointmentNumber(generatorService.generatorAppointmentIdentifier());

        commonKafkaProducer.publishAppointmentRescheduledEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_RESCHEDULE.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " rescheduled successfully.")
                .eventType(EventType.APPOINTMENT_RESCHEDULED)
                .build());

        return appointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse editAppointment(Long id, AppointmentRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No such appointment found!"));

        Appointment appointmentStatus = appointmentRepository.findBySchedule_Id(request.scheduleId());

        if (appointmentStatus.getStatus().equals(AppointmentStatus.CONFIRMED))
            throw new AppointmentModificationIsUnavailableException("After confirmation appointments cannot be modified");

        appointment.setId(appointment.getId());
        appointment.setStatus(appointment.getStatus());
        appointment.setVisitReason(request.visitReason());
        appointment.setPatient(appointment.getPatient());

        commonKafkaProducer.publishAppointmentUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_UPDATE.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " updated successfully.")
                .eventType(EventType.APPOINTMENT_UPDATED)
                .build());

        return appointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("No such appointment found!"));

        commonKafkaProducer.publishAppointmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_READ.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " retrieved successfully.")
                .eventType(EventType.APPOINTMENT_READ)
                .build());

        return appointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse getAppointment(LocalDate appointmentDate, String patientNationalId) {

        Appointment appointment =
                appointmentRepository.findByAppointmentDateAndPatient_nationalId(appointmentDate, Long.valueOf(patientNationalId))
                        .orElseThrow(() -> new AppointmentNotFoundException("No such appointment found!"));

        commonKafkaProducer.publishAppointmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_READ.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " retrieved successfully.")
                .eventType(EventType.APPOINTMENT_READ)
                .build());

        return appointmentResponse(appointment);
    }

    // By Patient, Doctor, and Staff
    @Transactional
    public List<AppointmentResponse> allAppointments(Doctor doctor, Patient patient, LocalDate appointmentDate, Long nationalId) {

        List<Appointment> appointmentList =
                appointmentRepository.findByDoctorOrPatientOrAppointmentDateOrPatient_NationalId(doctor, patient,
                        appointmentDate, nationalId);

        if (appointmentList.isEmpty())
            throw new PersonnelNotFoundException("No such record found!");

        commonKafkaProducer.publishAppointmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_READ.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("All appointments retrieved successfully.")
                .eventType(EventType.APPOINTMENT_READ)
                .build());

        return appointmentList.stream().map(this::appointmentResponse).toList();
    }

    // By Patient
    @Transactional
    public void cancelAppointment(Long id, String cancellationReason) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (SecurityUtils.hasAuthority("ROLE_PATIENT")) {
            if (!appointment.getPatient().getId().equals(SecurityUtils.currentUserId()))
                throw new AccessDeniedException("You cannot cancel another patient's appointment.");

        } else if (!SecurityUtils.hasAuthority("ROLE_RECEPTIONIST")) {
            throw new AccessDeniedException("You are not allowed to cancel this appointment.");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED)
            throw new AppointmentModificationIsUnavailableException("This appointment cannot be cancelled.");

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(cancellationReason);

        commonKafkaProducer.publishAppointmentCanceledEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_CANCEL.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() +
                        " canceled by user " + SecurityUtils.currentUserId() + " successfully")
                .eventType(EventType.APPOINTMENT_CANCELED)
                .build());
    }

    // By Admin only!
    @Transactional
    public void deleteAppointment(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found."));

        appointmentRepository.deleteById(appointment.getId());

        commonKafkaProducer.publishAppointmentDeletedEvent(CoreEvent.builder()
                .userId(appointment.getPatient().getId())
                .email(appointment.getPatient().getEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_DELETE.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " deleted successfully.")
                .eventType(EventType.APPOINTMENT_DELETED)
                .build());

    }

    // By Patient
    @Transactional
    public void confirmAppointment(Long id, AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found."));

        if (!status.equals(AppointmentStatus.SCHEDULED))
            throw new AppointmentModificationIsUnavailableException("Appointment cannot be confirmed.");

        appointment.setId(id);
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        commonKafkaProducer.publishAppointmentConfirmedEvent(CoreEvent.builder()
                .userId(appointment.getPatient().getId())
                .email(appointment.getPatient().getEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_CONFIRM.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " confirmed successfully.")
                .eventType(EventType.APPOINTMENT_CONFIRMED)
                .build());
    }

    // By Staff
    @Transactional
    public AppointmentResponse checkInAppointment(Long nationalId) {

        Appointment appointment = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new RecordNotFoundException("No scheduled appointment with this national id."));

        appointment.setStatus(AppointmentStatus.CHECKED_IN);

        Patient patient = patientRepository.findPatientByNationalId(nationalId)
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        patient.setArrival(Boolean.TRUE);

        commonKafkaProducer.publishPatientUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_UPDATE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " updated successfully.")
                .eventType(EventType.PATIENT_UPDATED)
                .build());

        commonKafkaProducer.publishAppointmentCheckInEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_CHECK_IN.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " checked in successfully.")
                .eventType(EventType.APPOINTMENT_CHECKED_IN)
                .build());

        return appointmentResponse(appointment);
    }

    // By Staff
    @Transactional
    public AppointmentResponse noShowAppointment(LocalDate appointmentDate, AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findByAppointmentDateAndStatus(appointmentDate, status)
                .orElseThrow(() -> new RecordNotFoundException("No such record found"));

        appointment.setStatus(AppointmentStatus.NO_SHOW);

        Patient patient = patientRepository.findPatientByNationalId(appointment.getPatient().getNationalId())
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        patient.setArrival(Boolean.FALSE);

        commonKafkaProducer.publishAppointmentNoShowEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_NO_SHOW.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("The patient for appointment " + appointment.getAppointmentNumber() + " did not arrive.")
                .eventType(EventType.APPOINTMENT_NO_SHOWED)
                .build());

        commonKafkaProducer.publishPatientUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_UPDATE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " updated successfully.")
                .eventType(EventType.PATIENT_UPDATED)
                .build());

        return appointmentResponse(appointment);
    }

    // By doctor
    @Transactional
    public AppointmentResponse inProgressAppointment(Long nationalId) {

        Appointment appointment = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new RecordNotFoundException("No scheduled appointment with this national id."));

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);

        commonKafkaProducer.publishAppointmentInProgressEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_IN_PROGRESS.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " is in progress.")
                .eventType(EventType.APPOINTMENT_IN_PROGRESS)
                .build());

        return appointmentResponse(appointment);
    }

    // By doctor, when prescription has submitted this method will be called!
    @Transactional
    public AppointmentResponse completedAppointment(Long nationalId) {

        Appointment appointment = appointmentRepository.findByPatient_NationalId(nationalId)
                .orElseThrow(() -> new RecordNotFoundException("No scheduled appointment with this national id."));

        appointment.setStatus(AppointmentStatus.COMPLETED);

        commonKafkaProducer.publishAppointmentCompletedEvent(CoreEvent.builder()
                .userId(appointment.getPatient().getId())
                .email(appointment.getPatient().getEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_COMPLETE.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Appointment " + appointment.getAppointmentNumber() + " completed successfully.")
                .eventType(EventType.APPOINTMENT_COMPLETED)
                .build());

        return appointmentResponse(appointment);
    }

    // run by the time of system! main page for staff
    @Transactional
    public Page<AppointmentResponse> todayAppointments(LocalDate todayAppointmentDate, Pageable pageable) {

        Page<Appointment> todayAppointments =
                appointmentRepository.findAllByTodayAppointments(todayAppointmentDate, pageable);

        if (todayAppointments.isEmpty())
            throw new BookedAppointmentNotFoundException("No booked appointment for today!");

        commonKafkaProducer.publishAppointmentReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.APPOINTMENT_READ.name())
                .entityName(EntityName.APPOINTMENT)
                .status(Status.SUCCESSFUL.name())
                .details("Today's all appointments retrieved successfully.")
                .eventType(EventType.APPOINTMENT_READ)
                .build());

        return todayAppointments.map(this::appointmentResponse);
    }

    private void suspendAppointment(Long id, AppointmentStatus status) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found."));

        if (status.equals(AppointmentStatus.SCHEDULED)) {
            appointment.setId(id);
            appointment.setStatus(AppointmentStatus.SUSPENDED);
            appointment.setRescheduled(Boolean.TRUE);
            appointmentRepository.save(appointment);
        }
    }

    private AppointmentResponse appointmentResponse(Appointment appointment) {

        return new AppointmentResponse(
                appointment.getId(),
                appointment.getSchedule().getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getVisitReason(),
                appointment.getCancellationReason(),
                appointment.getRescheduled(),
                appointment.getAppointmentNumber());
    }
}
