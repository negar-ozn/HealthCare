package com.healthcare.healthcare_api.personnel;

import com.healthcare.healthcare_api.shared.enums.PersonnelRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, Long> {

    @Query(value = "SELECT S FROM Personnel S WHERE S.firstName=:firstName OR S.lastName=:lastName")
    List<Personnel> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Personnel> findByUserId(Long userId);

    List<Personnel> findByRole(PersonnelRoleName role);

    @Query(value = "SELECT P FROM Personnel P WHERE P.id=:personnelId AND P.role=:role")
    Optional<Personnel> findByIdAndRole(Long personnelId, PersonnelRoleName role);
}
