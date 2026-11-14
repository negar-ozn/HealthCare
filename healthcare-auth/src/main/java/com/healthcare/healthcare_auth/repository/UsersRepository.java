package com.healthcare.healthcare_auth.repository;

import com.healthcare.healthcare_auth.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username); //During login
    Optional<Users> findByEmail(String email); //During login
    Optional<Users> findByUsernameOrEmail(String username, String email); //During login
    Boolean existsByUsername(String username); //During registration
    Boolean existsByEmail(String email); //During registration
    @Query("""
       SELECT DISTINCT u
       FROM Users u
       LEFT JOIN FETCH u.roles r
       LEFT JOIN FETCH r.permissions
       WHERE u.username = :username
       """)
    Optional<Users> findByUsernameWithRolesAndPermissions(String username);
}
