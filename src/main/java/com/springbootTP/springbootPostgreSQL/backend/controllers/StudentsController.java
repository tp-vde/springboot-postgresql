package com.springbootTP.springbootPostgreSQL.backend.controllers;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import com.springbootTP.springbootPostgreSQL.backend.service.IStudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentsController {

    private final IStudentsService studentsService;

    @GetMapping("/all-students")
    public ResponseEntity<List<Students>> getAllStudents() {
        List<Students> students = studentsService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/code")
    public ResponseEntity<List<Students>> getStudentsByCode(@RequestParam String code) {
        Optional<Students> student = studentsService.getStudentsByCode(code);
        return student.map(students -> ResponseEntity.ok(List.of(students)))
                .orElse(ResponseEntity.ok(List.of()));
    }

    @GetMapping("/email")
    public ResponseEntity<List<Students>> getStudentsByEmail(@RequestParam String email) {
        Optional<Students> student = studentsService.getStudentsByEmail(email);
        return student.map(students -> ResponseEntity.ok(List.of(students)))
                .orElse(ResponseEntity.ok(List.of()));
    }

    @PostMapping("/save-student")
    public ResponseEntity<Students> saveStudents(@RequestBody Students student) {
        Students savedStudent = studentsService.saveStudents(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PutMapping("/code")
    public ResponseEntity<Students> updateStudents(@RequestBody Students student, @RequestParam String code) {
        Students updatedStudent = studentsService.updateStudents(student, code);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/code")
    public ResponseEntity<Void> deleteStudentsByCode(@RequestParam String code) {
        studentsService.deleteStudentsByCode(code);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/email")
    public ResponseEntity<String> deleteStudentsByEmail(@RequestParam String email) {
        Optional<Students> student = studentsService.getStudentsByEmail(email);

        if (student.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with email " + email + " does not exist");
        }

        studentsService.deleteStudentsByEmail(email);
        return ResponseEntity.ok("Student with email " + email + " has been deleted successfully.");
    }

}