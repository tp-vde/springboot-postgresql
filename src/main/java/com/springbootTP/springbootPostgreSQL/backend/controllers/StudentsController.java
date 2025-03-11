package com.springbootTP.springbootPostgreSQL.backend.controllers;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import com.springbootTP.springbootPostgreSQL.backend.service.IStudentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour la gestion des étudiants.
 */
@RestController
@RequestMapping("/api/students") // Définition du chemin principal
@RequiredArgsConstructor
public class StudentsController {

    private final IStudentsService studentsService;

    /**
     * Récupère la liste de tous les étudiants.
     * @return Liste des étudiants.
     */
    @GetMapping("/all-students")
    public List<Students> getAllStudents() {
        return studentsService.getAllStudents();
    }

    /**
     * Récupère un étudiant par son code.
     * @param code Code unique de l'étudiant.
     * @return L'étudiant correspondant, s'il existe.
     */
    @GetMapping("/code/{code}")
    public Optional<Students> getStudentsByCode(@PathVariable String code) {
        return studentsService.getStudentsByCode(code);
    }

    /**
     * Récupère un étudiant par son email.
     * @param email Email unique de l'étudiant.
     * @return L'étudiant correspondant, s'il existe.
     */
    @GetMapping("/email/{email}")
    public Optional<Students> getStudentsByEmail(@PathVariable String email) {
        return studentsService.getStudentsByEmail(email);
    }

    /**
     * Enregistre un nouvel étudiant.
     * @param student Objet étudiant à enregistrer.
     * @return L'étudiant enregistré.
     */
    @PostMapping("/save-student") // Correction du chemin
    public ResponseEntity<Students> saveStudents (@RequestBody Students student) {
        Students students = studentsService.saveStudents(student);
        return ResponseEntity.ok(students);
    }

    /**
     * Met à jour les informations d'un étudiant existant.
     * @param student Objet étudiant contenant les nouvelles informations.
     * @param code Code de l'étudiant à mettre à jour.
     * @return L'étudiant mis à jour.
     */
    @PutMapping("/{code}")
    public Students updateStudents(@RequestBody Students student, @PathVariable String code) {
        return studentsService.updateStudents(student, code);
    }

    /**
     * Supprime un étudiant par son code.
     * @param code Code de l'étudiant à supprimer.
     */
    @DeleteMapping("/code/{code}")
    public void deleteStudentsByCode(@PathVariable String code) {
        studentsService.deleteStudentsByCode(code);
    }

    /**
     * Supprime un étudiant par son email.x
     * @param email Email de l'étudiant à supprimer.
     */
    @DeleteMapping("/email/{email}")
    public ResponseEntity<String> deleteStudentsByEmail(@PathVariable String email) {
        Optional<Students> student = studentsService.getStudentsByEmail(email);

        if (student.isEmpty()) {
            throw new RuntimeException("Student with email " + email + " does not exist");
        }

        studentsService.deleteStudentsByEmail(email);
        return ResponseEntity.ok("Student with email " + email + " has been deleted successfully.");
    }

}
