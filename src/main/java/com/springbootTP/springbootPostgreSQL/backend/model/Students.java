package com.springbootTP.springbootPostgreSQL.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(name = "vde_students",
        indexes = {@Index(name = "vde_students_code_idx", columnList = "code")},
        uniqueConstraints = @UniqueConstraint(name = "vde_students_unique_idx",
                columnNames = {"code", "email"}))
public class Students {

    @Id
    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone", nullable = false, length = 100)
    private String phone;

    @Column(name = "speciality", nullable = false, length = 100)
    private String speciality;

    @Column(name = "entry_at", nullable = false)
    private LocalDate entryAt;

    @Column(name = "first_departure_mission_at")
    private LocalDate firstDepartureMissionAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
