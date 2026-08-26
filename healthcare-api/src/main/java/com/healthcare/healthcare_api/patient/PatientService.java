package com.healthcare.healthcare_api.patient;

import com.healthcare.healthcare_api.personnel.PersonnelService;
import com.healthcare.healthcare_api.shared.enums.Gender;
import com.healthcare.healthcare_api.shared.enums.Status;
import com.healthcare.healthcare_api.shared.exception.NoSuchPatientFoundException;
import com.healthcare.healthcare_api.shared.exception.PatientAlreadyExistsException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final IdentifierGeneratorService generatorService;
    private final PersonnelService personnelService;

    public PatientService(PatientRepository patientRepository, IdentifierGeneratorService generatorService, PersonnelService personnelService) {
        this.patientRepository = patientRepository;
        this.generatorService = generatorService;
        this.personnelService = personnelService;
    }

    public Patient addPatient(PatientRequest patientRequest) {

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
//                .version()
                .createdAt(Instant.now())
                .build();
        return patientRepository.save(patient);
    }

    public Patient editPatient(Long id, PatientRequest patientRequest) {

        Patient patientOp = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));
        Patient patient = Patient.builder()
                .id(patientOp.getId())
                .firstName(patientRequest.firstName())
                .lastName(patientRequest.lastName())
                .birthdate(patientRequest.birthdate())
                .gender(Gender.valueOf(patientRequest.gender()))
                .nationalId(patientRequest.nationalId())
                .phone(patientRequest.phone())
                .email(patientRequest.email())
                .address(patientRequest.address())
                .status(Status.ACTIVE)
                .patientNumber(patientRequest.patientNumber())
//                .version()
                .updatedAt(Instant.now())
                .insurance(patientRequest.insurance())
                .build();
        return patientRepository.save(patient);
    }

    public PatientResponse getPatient(Long id) {

        Patient patientOp = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));

        return PatientResponse.builder()
                .firstName(patientOp.getFirstName())
                .lastName(patientOp.getFirstName())
                .birthdate(patientOp.getBirthdate())
                .gender(String.valueOf(patientOp.getGender()))
                .nationalId(patientOp.getNationalId())
                .phone(patientOp.getPhone())
                .email(patientOp.getEmail())
                .address(patientOp.getAddress())
                .arrival(patientOp.getArrival())
                .status(patientOp.getStatus())
                .patientNumber(patientOp.getPatientNumber())
//                .version()
                .createdAt(patientOp.getCreatedAt())
                .updatedAt(patientOp.getUpdatedAt())
                .insurance(patientOp.getInsurance())
                .build();
    }

    public List<PatientResponse> allPatient(String firstName, String lastName, Long nationalId) {

        List<Patient> patientList =
                patientRepository.findAllByFirstNameOrLastNameOrNationalId(firstName, lastName, nationalId);
        if (patientList.isEmpty()) {
            throw new NoSuchPatientFoundException("No such patient found");
        }
        return patientList.stream().map(patient -> PatientResponse.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .birthdate(patient.getBirthdate())
                .gender(String.valueOf(patient.getGender()))
                .nationalId(patient.getNationalId())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .arrival(patient.getArrival())
                .insurance(patient.getInsurance())
                .status(patient.getStatus())
                .patientNumber(patient.getPatientNumber())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build()).toList();
    }

    public void deletePatient(Long id) {

        Patient patientOp = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));
        patientRepository.deleteById(patientOp.getId());
    }

    public void inactivate(Long id) {

        Patient patientOp = patientRepository.findById(id)
                .orElseThrow(() -> new NoSuchPatientFoundException("No such patient found"));
        Patient patient = Patient.builder()
                .id(patientOp.getId())
                .status(Status.INACTIVE)
                .build();
        patientRepository.save(patient);
    }

    public List<PatientResponse> patientHistory(Long id, Long nationalId) {

        List<Patient> patientList = patientRepository.findAllByIdOrNationalId(id, nationalId);
        if (patientList.isEmpty()) {
            throw new NoSuchPatientFoundException("No such patient found");
        }
        return patientList.stream().map(patient -> PatientResponse.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .birthdate(patient.getBirthdate())
                .gender(String.valueOf(patient.getGender()))
                .nationalId(patient.getNationalId())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .arrival(patient.getArrival())
                .insurance(patient.getInsurance())
                .status(patient.getStatus())
                .patientNumber(patient.getPatientNumber())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build()).toList();
    }
}