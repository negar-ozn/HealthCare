package com.healthcare.healthcare_auth.repository;

import com.healthcare.healthcare_auth.entity.Permission;
import com.healthcare.healthcare_auth.enums.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByDescription(Permissions name);
}
