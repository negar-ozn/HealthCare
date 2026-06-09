package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("SELECT U FROM USERS U WHERE U.username=:username OR U.email=:email")
    Optional<Users> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
}
