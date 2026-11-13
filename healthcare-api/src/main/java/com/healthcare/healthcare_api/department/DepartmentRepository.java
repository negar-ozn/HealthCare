package com.healthcare.healthcare_api.department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = "SELECT d FROM Department d WHERE d.name=:name")
    List<Department> findAllByName(String name);

    @Query(value = "SELECT d FROM Department d WHERE d.name=:name")
    Optional<Department> findByName(String name);
}
