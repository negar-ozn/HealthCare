package com.healthcare.healthcare_auth.repository;

import com.healthcare.healthcare_auth.entity.Role;
import com.healthcare.healthcare_auth.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(UserRole role);
}
