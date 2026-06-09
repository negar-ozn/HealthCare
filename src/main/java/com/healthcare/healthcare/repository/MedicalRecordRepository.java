package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Query(value = "SELECT m FROM MEDICAL_RECORD m WHERE m.patient.nationalId=:nationalId")
    List<MedicalRecord> findByPatient_NationalId(Long nationalId);
}
