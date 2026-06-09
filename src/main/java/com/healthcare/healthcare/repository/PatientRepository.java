package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(value = "SELECT P FROM PATIENT P WHERE P.nationalId=:nationalId")
    Optional<Patient> findPatientByNationalId(Long nationalId);

    @Query(value = "SELECT P FROM PATIENT P WHERE P.id=:id OR P.nationalId=:nationalId")
    List<Patient> findAllByIdOrNationalId(Long id, Long nationalId);

    @Query(value = "SELECT P FROM PATIENT P WHERE P.firstName=:firstName OR P.lastName=:lastName OR P.nationalId=:nationalId")
    List<Patient> findAllByFirstNameOrLastNameOrNationalId(String firstName, String lastName, Long nationalId);
}
