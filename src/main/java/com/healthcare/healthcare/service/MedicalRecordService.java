package com.healthcare.healthcare.service;

import com.healthcare.healthcare.dto.MedicalRecordDto;
import com.healthcare.healthcare.entitiy.MedicalRecord;
import com.healthcare.healthcare.entitiy.Patient;
import com.healthcare.healthcare.exception.MedicalRecordAlreadyExistsException;
import com.healthcare.healthcare.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    //All services run by Doctor!
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public MedicalRecord addMedicalRecord(MedicalRecordDto medicalRecordDto) {
        Optional<MedicalRecord> optional = medicalRecordRepository.findById(medicalRecordDto.getId());
        if (optional.isPresent()) {
            try {
                throw new MedicalRecordAlreadyExistsException("medical record is already existed");
            } catch (MedicalRecordAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setSymptoms(medicalRecordDto.getSymptoms());
        medicalRecord.setDoctor(medicalRecordDto.getDoctor());
        medicalRecord.setPatient(medicalRecordDto.getPatient());
        medicalRecord.setVisitDate(medicalRecordDto.getVisitDate());
        medicalRecord.setNotes(medicalRecordDto.getNotes());
        medicalRecord.setPrescriptions(medicalRecordDto.getPrescriptions());
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord editMedicalRecord(Long id, MedicalRecordDto medicalRecordDto) {
        Optional<MedicalRecord> optional = medicalRecordRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        MedicalRecord medicalRecord = optional.get();
        medicalRecordDto.setId(id);
        medicalRecord.setDoctor(medicalRecordDto.getDoctor());
        medicalRecord.setPatient(medicalRecordDto.getPatient());
        medicalRecord.setSymptoms(medicalRecordDto.getSymptoms());
        medicalRecord.setVisitDate(medicalRecordDto.getVisitDate());
        medicalRecord.setNotes(medicalRecordDto.getNotes());
        medicalRecord.setPrescriptions(medicalRecordDto.getPrescriptions());
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecordDto getMedicalRecord(Long id) {
        Optional<MedicalRecord> optional = medicalRecordRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        MedicalRecord medicalRecord = optional.get();
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        medicalRecordDto.setId(medicalRecord.getId());
        medicalRecordDto.setPatient(medicalRecord.getPatient());
        medicalRecordDto.setDoctor(medicalRecord.getDoctor());
        medicalRecordDto.setSymptoms(medicalRecord.getSymptoms());
        medicalRecordDto.setVisitDate(medicalRecord.getVisitDate());
        medicalRecordDto.setNotes(medicalRecord.getNotes());
        medicalRecordDto.setPrescriptions(medicalRecord.getPrescriptions());
        return null;
    }

    public List<MedicalRecordDto> allMedicalRecord(Long nationalId) {
        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findByPatient_NationalId(nationalId);
        if (medicalRecordList.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        List<MedicalRecordDto> medicalRecordDtoList = new ArrayList<>();
        MedicalRecordDto medicalRecordDto = new MedicalRecordDto();
        for (MedicalRecord medicalRecord : medicalRecordList) {
            medicalRecordDto.setPatient(medicalRecord.getPatient());
            medicalRecordDto.setDoctor(medicalRecord.getDoctor());
            medicalRecordDto.setSymptoms(medicalRecord.getSymptoms());
            medicalRecordDto.setVisitDate(medicalRecord.getVisitDate());
            medicalRecordDto.setNotes(medicalRecord.getNotes());
            medicalRecordDto.setPrescriptions(medicalRecord.getPrescriptions());
            medicalRecordDto.setId(medicalRecord.getId());
            medicalRecordDtoList.add(medicalRecordDto);
        }
        return medicalRecordDtoList;
    }

    public void deleteMedicalRecord(Long id) {
        Optional<MedicalRecord> optional = medicalRecordRepository.findById(id);
        if (optional.isEmpty()) {
            try {
                throw new NoSuchRecordFoundException("no such record found");
            } catch (NoSuchRecordFoundException e) {
                throw new RuntimeException(e);
            }
        }
        MedicalRecord medicalRecord = optional.get();
        medicalRecordRepository.deleteById(medicalRecord.getId());
    }
}
