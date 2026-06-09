package com.healthcare.healthcare.repository;

import com.healthcare.healthcare.entitiy.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Users, Long> {
}
