package com.healthcare.healthcare_api.medicalRecord;

import com.healthcare.healthcare_api.shared.exception.MedicalRecordAlreadyExistsException;
import com.healthcare.healthcare_api.shared.exception.NoSuchRecordFoundException;
import com.healthcare.healthcare_api.shared.util.IdentifierGeneratorService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MedicalRecordService {

    //All services run by Doctor!
    private final MedicalRecordRepository medicalRecordRepository;
    private final IdentifierGeneratorService generatorService;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, IdentifierGeneratorService generatorService) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.generatorService = generatorService;
    }

    public MedicalRecord addMedicalRecord(MedicalRecordRequest medicalRecordRequest) {

        medicalRecordRepository.findById(medicalRecordRequest.id())
                .orElseThrow(() -> new MedicalRecordAlreadyExistsException("medical record is already existed"));
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .visitDate(medicalRecordRequest.visitDate())
                .symptoms(medicalRecordRequest.symptoms())
                .notes(medicalRecordRequest.notes())
                .prescriptions(medicalRecordRequest.prescriptions())
                .prescriptionsNumber(generatorService.generatePrescriptionIdentifier())
//                .version()
                .patient(medicalRecordRequest.patient())
                .doctor(medicalRecordRequest.doctor())
                .createdAt(Instant.now())
                .build();
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord editMedicalRecord(Long id, MedicalRecordRequest medicalRecordRequest) {

        MedicalRecord medicalRecordOp = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .id(medicalRecordOp.getId())
                .visitDate(medicalRecordRequest.visitDate())
                .symptoms(medicalRecordRequest.symptoms())
                .notes(medicalRecordRequest.notes())
                .prescriptions(medicalRecordRequest.prescriptions())
                .prescriptionsNumber(medicalRecordRequest.prescriptionsNumber())
//                .version()
                .patient(medicalRecordRequest.patient())
                .doctor(medicalRecordRequest.doctor())
                .updatedAt(Instant.now())
                .build();
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecordResponse getMedicalRecord(Long id) {

        MedicalRecord medicalRecordOp = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        return MedicalRecordResponse.builder()
                .patient(medicalRecordOp.getPatient())
                .doctor(medicalRecordOp.getDoctor())
                .visitDate(medicalRecordOp.getVisitDate())
                .symptoms(medicalRecordOp.getSymptoms())
                .notes(medicalRecordOp.getNotes())
                .prescriptions(medicalRecordOp.getPrescriptions())
                .prescriptionsNumber(medicalRecordOp.getPrescriptionsNumber())
                .createdAt(medicalRecordOp.getCreatedAt())
                .updatedAt(medicalRecordOp.getUpdatedAt())
                .build();
    }

    public List<MedicalRecordResponse> allMedicalRecord(Long nationalId) {

        List<MedicalRecord> medicalRecordList = medicalRecordRepository.findByPatient_NationalId(nationalId);
        if (medicalRecordList.isEmpty()) {
            throw new NoSuchRecordFoundException("no such record found");
        }
        return medicalRecordList.stream().map(medicalRecord -> MedicalRecordResponse.builder()
                .patient(medicalRecord.getPatient())
                .doctor(medicalRecord.getDoctor())
                .visitDate(medicalRecord.getVisitDate())
                .symptoms(medicalRecord.getSymptoms())
                .notes(medicalRecord.getNotes())
                .prescriptions(medicalRecord.getPrescriptions())
                .prescriptionsNumber(medicalRecord.getPrescriptions())
                .createdAt(medicalRecord.getCreatedAt())
                .updatedAt(medicalRecord.getUpdatedAt())
                .build()).toList();
    }

    public void deleteMedicalRecord(Long id) {

        MedicalRecord medicalRecordOp = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordFoundException("no such record found"));
        medicalRecordRepository.deleteById(medicalRecordOp.getId());
    }
}
