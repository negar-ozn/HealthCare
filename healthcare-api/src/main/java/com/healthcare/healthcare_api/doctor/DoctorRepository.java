package com.healthcare.healthcare_api.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("""
    SELECT d FROM Doctor d
    WHERE LOWER(d.personnel.firstName) LIKE LOWER(CONCAT('%', :firstname, '%'))
       OR LOWER(d.personnel.lastName) LIKE LOWER(CONCAT('%', :lastname, '%'))
       OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :specialization, '%'))
       OR d.licenceNumber = :licenceNumber
    """)
    List<Doctor> findByFirstNameOrLastNameOrSpecializationOrLicenceNumber(String firstname, String lastname,
                                                                          String specialization, Long licenceNumber);
}
