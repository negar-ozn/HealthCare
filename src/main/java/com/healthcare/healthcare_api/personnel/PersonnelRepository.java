package com.healthcare.healthcare_api.personnel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    @Query(value = "SELECT S FROM Personnel S WHERE S.firstName=:firstName OR S.lastName=:lastName")
    List<Personnel> findByFirstNameAndLastName(String firstName, String lastName);
}
