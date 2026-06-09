package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.PatientDto;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.exception.NoSuchPatientFoundException;
import com.healthcare.healthcare.exception.PatientAlreadyExistsException;
import com.healthcare.healthcare.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(PatientDto patientDto) {
        Optional<Patient> optional = patientRepository.findById(patientDto.getId());
        if (optional.isPresent()) {
            try {
                throw new PatientAlreadyExistsException("Patient already existed");
            } catch (PatientAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient = new Patient();
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setGender(patientDto.getGender());
        patient.setNationalId(patientDto.getNationalId());
        patient.setBirthdate(patientDto.getBirthdate());
        patient.setPhone(patientDto.getPhone());
        patient.setAddress(patientDto.getAddress());
        return patientRepository.save(patient);
    }

    public Patient editPatient(Long id, PatientDto patientDto) {
        Optional<Patient> optional = patientRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient = optional.get();
        patient.setId(id);
        patient.setGender(patientDto.getGender());
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setBirthdate(patientDto.getBirthdate());
        patient.setPhone(patientDto.getPhone());
        patient.setAddress(patientDto.getAddress());
        patient.setNationalId(patientDto.getNationalId());
        patient.setInsurance(patientDto.getInsurance());
        patient.setStatus(patientDto.getStatus());
        return patientRepository.save(patient);
    }

    public PatientDto getPatient(Long id) {
        Optional<Patient> optional = patientRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient = optional.get();
        PatientDto patientDto = new PatientDto();
        patientDto.setId(id);
        patientDto.setNationalId(patient.getNationalId());
        patientDto.setFirstName(patient.getFirstName());
        patientDto.setLastName(patient.getLastName());
        patientDto.setGender(patient.getGender());
        patientDto.setBirthdate(patient.getBirthdate());
        patientDto.setPhone(patient.getPhone());
        patientDto.setAddress(patient.getAddress());
        patientDto.setInsurance(patient.getInsurance());
        patientDto.setStatus(patient.getStatus());
        return patientDto;
    }

    public List<PatientDto> allPatient(String firstName, String lastName, Long nationalId) {
        List<Patient> patientList = patientRepository.findAllByFirstNameOrLastNameOrNationalId(firstName, lastName, nationalId);
        if (patientList.isEmpty()) {
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<PatientDto> patientDtoList = new ArrayList<>();
        PatientDto patientDto = new PatientDto();
        for (Patient patient : patientList) {
            patientDto.setId(patient.getId());
            patientDto.setFirstName(patient.getFirstName());
            patientDto.setLastName(patient.getLastName());
            patientDto.setGender(patient.getGender());
            patientDto.setBirthdate(patient.getBirthdate());
            patientDto.setPhone(patient.getPhone());
            patientDto.setAddress(patient.getAddress());
            patientDto.setNationalId(patient.getNationalId());
            patientDto.setInsurance(patient.getInsurance());
            patientDto.setStatus(patient.getStatus());
            patientDtoList.add(patientDto);
        }
        return patientDtoList;
    }

    public void deletePatient(Long id) {
        Optional<Patient> optional = patientRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        }
        Patient patient = optional.get();
        patientRepository.deleteById(patient.getId());
    }

    public Patient patientExist(Long patientId) {
        Optional<Patient> optional = patientRepository.findById(patientId);
        if (optional.isEmpty())
            try {
                throw new NoSuchPatientFoundException("No such patient found!");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        return optional.get();
    }

    public List<PatientDto> patientHistory(Long id, Long nationalId) {
        List<Patient> patientList = patientRepository.findAllByIdOrNationalId(id, nationalId);
        if (patientList.isEmpty()) {
            try {
                throw new NoSuchPatientFoundException("No such patient found");
            } catch (NoSuchPatientFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<PatientDto> patientDtoList = new ArrayList<>();
        PatientDto patientDto = new PatientDto();
        for (Patient patient : patientList) {
            patientDto.setId(patient.getId());
            patientDto.setFirstName(patient.getFirstName());
            patientDto.setLastName(patient.getLastName());
            patientDto.setGender(patient.getGender());
            patientDto.setBirthdate(patient.getBirthdate());
            patientDto.setPhone(patient.getPhone());
            patientDto.setAddress(patient.getAddress());
            patientDto.setNationalId(patient.getNationalId());
            patientDto.setInsurance(patient.getInsurance());
            patientDto.setStatus(patient.getStatus());
            patientDtoList.add(patientDto);
        }
        return patientDtoList;
    }
}


//        PatientDto patientDto = new PatientDto();
//        patientDto.setId(patientId);
//        patientDto.setGender(patient.getGender());
//        patientDto.setFirstName(patient.getFirstName());
//        patientDto.setLastName(patient.getLastName());
//        patientDto.setBirthdate(patient.getBirthdate());
//        patientDto.setPhone(patient.getPhone());
//        patientDto.setAddress(patient.getAddress());
//        patientDto.setNationalId(patient.getNationalId());
//        patientDto.setInsurance(patient.getInsurance());
//        patientDto.setStatus(patient.getStatus());