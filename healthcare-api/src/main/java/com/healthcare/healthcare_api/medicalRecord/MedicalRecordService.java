package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.appointment.AppointmentResponse;
import com.healthcare.healthcare_api.appointment.AppointmentService;
import com.healthcare.healthcare_api.patient.Patient;
import com.healthcare.healthcare_api.patient.PatientRepository;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.MedicalRecordAlreadyExistsException;
import com.healthcare.healthcare_api.shared.exception.PatientNotFoundException;
import com.healthcare.healthcare_api.shared.exception.RecordNotFoundException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MedicalRecordService {

    //All services run by Doctor!
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final IdentifierGeneratorService generatorService;
    private final AppointmentService appointmentService;
    private final CommonKafkaProducer commonKafkaProducer;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository,
                                IdentifierGeneratorService generatorService, AppointmentService appointmentService,
                                CommonKafkaProducer commonKafkaProducer) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.generatorService = generatorService;
        this.appointmentService = appointmentService;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public MedicalRecordResponse addMedicalRecord(MedicalRecordRequest medicalRecordRequest) {

        medicalRecordRepository.findById(medicalRecordRequest.id())
                .orElseThrow(() -> new MedicalRecordAlreadyExistsException("medical record is already existed"));

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .visitDate(medicalRecordRequest.visitDate())
                .symptoms(medicalRecordRequest.symptoms())
                .notes(medicalRecordRequest.notes())
                .prescriptions(medicalRecordRequest.prescriptions())
                .prescriptionsNumber(generatorService.generatePrescriptionIdentifier())
                .patient(medicalRecordRequest.patient())
                .doctor(medicalRecordRequest.doctor())
                .createdAt(Instant.now())
                .build();

        MedicalRecord savedMedicalRecord = medicalRecordRepository.save(medicalRecord);

        commonKafkaProducer.publishMedicalRecordCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_CREATE.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("Medical record " + savedMedicalRecord.getId() + " created successfully.")
                .eventType(EventType.MEDICAL_RECORD_CREATED)
                .build());

        return medicalRecordResponse(medicalRecord);
    }

    @Transactional
    public MedicalRecordResponse editMedicalRecord(Long id, MedicalRecordRequest medicalRecordRequest) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        medicalRecord.setVisitDate(medicalRecordRequest.visitDate());
        medicalRecord.setSymptoms(medicalRecordRequest.symptoms());
        medicalRecord.setNotes(medicalRecordRequest.notes());
        medicalRecord.setPrescriptions(medicalRecordRequest.prescriptions());
        medicalRecord.setPrescriptionsNumber(medicalRecordRequest.prescriptionsNumber());
        medicalRecord.setPatient(medicalRecordRequest.patient());
        medicalRecord.setDoctor(medicalRecordRequest.doctor());

        commonKafkaProducer.publishMedicalRecordUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_UPDATE.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("Medical record " + medicalRecord.getId() + " updated successfully.")
                .eventType(EventType.MEDICAL_RECORD_UPDATED)
                .build());

        return medicalRecordResponse(medicalRecord);
    }

    @Transactional
    public MedicalRecordResponse getMedicalRecord(Long id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        commonKafkaProducer.publishMedicalRecordReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_READ.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("Medical record " + medicalRecord.getId() + " retrieved successfully.")
                .eventType(EventType.MEDICAL_RECORD_READ)
                .build());

        return medicalRecordResponse(medicalRecord);
    }

    @Transactional
    public List<MedicalRecordResponse> allMedicalRecord(Long nationalId) {

        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findByPatient_NationalId(nationalId);

        if (medicalRecordList.isEmpty())
            throw new RecordNotFoundException("no such record found");

        commonKafkaProducer.publishMedicalRecordReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_READ.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("All medical records retrieved successfully.")
                .eventType(EventType.MEDICAL_RECORD_READ)
                .build());

        return medicalRecordList.stream().map(this::medicalRecordResponse).toList();
    }

    @Transactional
    public void deleteMedicalRecord(Long id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        commonKafkaProducer.publishMedicalRecordDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_DELETE.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("All medical records retrieved successfully.")
                .eventType(EventType.MEDICAL_RECORD_DELETED)
                .build());

        medicalRecordRepository.deleteById(medicalRecord.getId());
    }

    @Transactional
    public AppointmentResponse submitMedicalRecord(Long id) {

        MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("no such record found"));

        Patient patient = patientRepository.findById(medicalRecord.getPatient().getId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found."));

        AppointmentResponse appointmentResponse = appointmentService.completedAppointment(patient.getNationalId());

        commonKafkaProducer.publishMedicalRecordCompletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.MEDICAL_RECORD_COMPLETE.name())
                .entityName(EntityName.MEDICAL_RECORD)
                .status(Status.SUCCESSFUL.name())
                .details("All medical records retrieved successfully.")
                .eventType(EventType.MEDICAL_RECORD_COMPLETED)
                .build());

        return appointmentResponse;
    }

    private MedicalRecordResponse medicalRecordResponse(MedicalRecord medicalRecord) {

        return new MedicalRecordResponse(
                medicalRecord.getPatient().getId(),
                medicalRecord.getDoctor().getId(),
                medicalRecord.getVisitDate(),
                medicalRecord.getSymptoms(),
                medicalRecord.getNotes(),
                medicalRecord.getPrescriptions(),
                medicalRecord.getPrescriptionsNumber()
        );
    }
}
