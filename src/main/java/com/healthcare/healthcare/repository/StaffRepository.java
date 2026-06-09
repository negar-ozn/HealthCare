package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query(value = "SELECT S FROM STAFF S WHERE S.firstName=:firstName OR S.lastName=:lastName")
    Optional<Staff> findByFirstNameAndLastName(String firstName, String lastName);
}
