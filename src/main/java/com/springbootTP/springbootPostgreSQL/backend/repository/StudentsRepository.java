package com.springbootTP.springbootPostgreSQL.backend.repository;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentsRepository extends JpaRepository <Students, String> {

    Optional<Students> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}
