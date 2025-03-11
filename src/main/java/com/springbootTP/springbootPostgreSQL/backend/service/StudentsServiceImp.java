package com.springbootTP.springbootPostgreSQL.backend.service;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import com.springbootTP.springbootPostgreSQL.backend.repository.StudentsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service gérant les opérations liées aux étudiants.
 */
@Service
public class StudentsServiceImp implements IStudentsService {

    // Injection du repository des étudiants pour interagir avec la base de données

    private StudentsRepository studentsRepository;

    @Autowired
    public StudentsServiceImp(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    /**
     * Enregistre un nouvel étudiant dans la base de données.
     * @param student L'objet étudiant à enregistrer.
     * @return L'étudiant enregistré.
     */
    @Override
    public Students saveStudents(Students student) {
        return studentsRepository.save(student);
    }

    /**
     * Récupère la liste de tous les étudiants.
     * @return Une liste contenant tous les étudiants enregistrés.
     */
    @Override
    public List<Students> getAllStudents() {
        return studentsRepository.findAll();
    }

    /**
     * Recherche un étudiant par son code unique.
     * @param code Le code de l'étudiant.
     * @return Un objet Optional contenant l'étudiant s'il est trouvé.
     */
    @Override
    public Optional<Students> getStudentsByCode(String code) {
        return studentsRepository.findById(code);
    }

    /**
     * Recherche un étudiant par son adresse e-mail unique.
     * @param email L'adresse e-mail de l'étudiant.
     * @return Un objet Optional contenant l'étudiant s'il est trouvé.
     */
    @Override
    public Optional<Students> getStudentsByEmail(String email) {
        return studentsRepository.findByEmail(email);
    }

    /**
     * Met à jour les informations d'un étudiant existant.
     * @param student L'objet contenant les nouvelles informations.
     * @param code Le code de l'étudiant (peut être null si email est utilisé).
     * @return L'objet étudiant mis à jour.
     * @throws RuntimeException si l'étudiant n'est pas trouvé.
     */
    @Override
    public Students updateStudents(Students student, String code) {
        // Rechercher l'étudiant par code
        Students existingStudent = studentsRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Student does not exist"));

        // Mise à jour des informations de l'étudiant
        existingStudent.setCode(student.getCode());
        existingStudent.setName(student.getName());
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setSpeciality(student.getSpeciality());
        existingStudent.setEntryAt(student.getEntryAt());
        existingStudent.setFirstDepartureMissionAt(student.getFirstDepartureMissionAt());

        // Sauvegarde des modifications dans la base de données
        return studentsRepository.save(existingStudent);
    }


    /**
     * Supprime un étudiant de la base de données en fonction de son code.
     * @param code Le code de l'étudiant à supprimer.
     */
    @Override
    public void deleteStudentsByCode(String code) {
        studentsRepository.deleteById(code);
    }

    /**
     * Supprime un étudiant de la base de données en fonction de son adresse e-mail.
     */
    @Override
    @Transactional
    public void deleteStudentsByEmail(String students) {
        studentsRepository.deleteByEmail(students);
    }
}
