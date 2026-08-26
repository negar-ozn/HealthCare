package com.healthcare.healthcare_api.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query(value = "SELECT d FROM Doctor d WHERE d.personnel.firstName=:firstname " +
            "OR d.personnel.lastName=:lastname OR d.specialization=:specialization OR d.licenceNumber=:licenceNumber")
    List<Doctor> findByFirstNameOrLastNameOrSpecializationOrLicenceNumber(String firstname, String lastname,
                                                                          String specialization, Long licenceNumber);
}
