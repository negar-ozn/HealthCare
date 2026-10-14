package com.healthcare.healthcare_api.patient;

import com.healthcare.common_kafka.enums.EntityName;
import com.healthcare.common_kafka.enums.EventType;
import com.healthcare.common_kafka.event.CoreEvent;
import com.healthcare.common_kafka.producer.CommonKafkaProducer;
import com.healthcare.healthcare_api.personnel.PersonnelService;
import com.healthcare.healthcare_api.security.SecurityUtils;
import com.healthcare.healthcare_api.shared.enums.ActionType;
import com.healthcare.healthcare_api.shared.enums.Gender;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.PatientNotFoundException;
import com.healthcare.healthcare_api.shared.exception.PatientAlreadyExistsException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final IdentifierGeneratorService generatorService;
    private final PersonnelService personnelService;
    private final CommonKafkaProducer commonKafkaProducer;

    public PatientService(PatientRepository patientRepository, IdentifierGeneratorService generatorService,
                          PersonnelService personnelService, CommonKafkaProducer commonKafkaProducer) {
        this.patientRepository = patientRepository;
        this.generatorService = generatorService;
        this.personnelService = personnelService;
        this.commonKafkaProducer = commonKafkaProducer;
    }

    @Transactional
    public PatientResponse addPatient(PatientRequest patientRequest) {

        patientRepository.findById(patientRequest.id()).
                orElseThrow(() -> new PatientAlreadyExistsException("Patient already existed"));

        Patient patient = Patient.builder()
                .firstName(patientRequest.firstName())
                .lastName(patientRequest.lastName())
                .birthdate(patientRequest.birthdate())
                .gender(personnelService.genderIdentifier(patientRequest.gender()))
                .nationalId(patientRequest.nationalId())
                .phone(patientRequest.phone())
                .email(patientRequest.email())
                .address(patientRequest.address())
                .status(Status.ACTIVE)
                .patientNumber(generatorService.generatePatientIdentifier())
                .insurance(patientRequest.insurance())
                .build();
        patientRepository.save(patient);

        commonKafkaProducer.publishPatientCreatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_CREATE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " created successfully.")
                .eventType(EventType.PATIENT_CREATED)
                .build());

        return patientResponse(patient);
    }

    @Transactional
    public PatientResponse editPatient(Long id, PatientRequest patientRequest) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        patient.setFirstName(patientRequest.firstName());
        patient.setLastName(patientRequest.lastName());
        patient.setBirthdate(patientRequest.birthdate());
        patient.setGender(Gender.valueOf(patientRequest.gender()));
        patient.setNationalId(patientRequest.nationalId());
        patient.setPhone(patientRequest.phone());
        patient.setEmail(patientRequest.email());
        patient.setAddress(patientRequest.address());
        patient.setStatus(Status.ACTIVE);
        patient.setPatientNumber(patientRequest.patientNumber());
        patient.setInsurance(patientRequest.insurance());

        commonKafkaProducer.publishPatientUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_UPDATE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " updated successfully")
                .eventType(EventType.PATIENT_UPDATED)
                .build());

        return patientResponse(patient);
    }

    @Transactional
    public PatientResponse getPatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        commonKafkaProducer.publishPatientReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_READ.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " retrieved successfully")
                .eventType(EventType.PATIENT_READ)
                .build());

        return patientResponse(patient);
    }

    @Transactional
    public List<PatientResponse> allPatient(String firstName, String lastName, Long nationalId) {

        List<Patient> patientList =
                patientRepository.findAllByFirstNameOrLastNameOrNationalId(firstName, lastName, nationalId);

        if (patientList.isEmpty())
            throw new PatientNotFoundException("No such patient found");

        commonKafkaProducer.publishPatientReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_READ.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("All patients retrieved successfully")
                .eventType(EventType.PATIENT_READ)
                .build());

        return patientList.stream().map(this::patientResponse).toList();
    }

    @Transactional
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        patientRepository.deleteById(patient.getId());

        commonKafkaProducer.publishPatientDeletedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_DELETE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " deleted successfully")
                .eventType(EventType.PATIENT_DELETED)
                .build());
    }

    @Transactional
    public void inactivate(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("No such patient found"));

        patient.setStatus(Status.INACTIVE);

        commonKafkaProducer.publishPatientUpdatedEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_UPDATE.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient " + patient.getId() + " inactivated successfully")
                .eventType(EventType.PATIENT_UPDATED)
                .build());
    }

    @Transactional
    public List<PatientResponse> patientHistory(Long id, Long nationalId) {

        List<Patient> patientList = patientRepository.findAllByIdOrNationalId(id, nationalId);

        if (patientList.isEmpty())
            throw new PatientNotFoundException("No such patient found");

        commonKafkaProducer.publishPatientReadEvent(CoreEvent.builder()
                .userId(SecurityUtils.currentUserId())
                .email(SecurityUtils.currentUserEmail())
                .username(SecurityUtils.currentUsername())
                .action(ActionType.PATIENT_READ.name())
                .entityName(EntityName.PATIENT)
                .status(Status.SUCCESSFUL.name())
                .details("Patient's history retrieved successfully")
                .eventType(EventType.PATIENT_READ)
                .build());

        return patientList.stream().map(this::patientResponse).toList();
    }

    private PatientResponse patientResponse(Patient patient) {

        return new PatientResponse(
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthdate(),
                patient.getGender(),
                patient.getNationalId(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getArrival(),
                patient.getInsurance(),
                patient.getStatus(),
                patient.getPatientNumber()
        );
    }
}