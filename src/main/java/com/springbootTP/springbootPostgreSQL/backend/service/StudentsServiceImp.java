package com.springbootTP.springbootPostgreSQL.backend.service;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import com.springbootTP.springbootPostgreSQL.backend.repository.StudentsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service gérant les opérations liées aux étudiants.
 */
@Service
public class StudentsServiceImp implements IStudentsService {

    private static final Logger logger = LoggerFactory.getLogger(StudentsServiceImp.class);
    private final StudentsRepository studentsRepository;

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
        logger.info("Tentative d'enregistrement d'un nouvel étudiant : {}", student);
        Students savedStudent = studentsRepository.save(student);
        logger.info("Étudiant enregistré avec succès : {}", savedStudent);
        return savedStudent;
    }

    /**
     * Récupère la liste de tous les étudiants.
     * @return Une liste contenant tous les étudiants enregistrés.
     */
    @Override
    public List<Students> getAllStudents() {
        logger.info("Récupération de la liste de tous les étudiants...");
        List<Students> students = studentsRepository.findAll();
        logger.info("Nombre total d'étudiants trouvés : {}", students.size());
        return students;
    }

    /**
     * Recherche un étudiant par son code unique.
     * @param code Le code de l'étudiant.
     * @return Un objet Optional contenant l'étudiant s'il est trouvé.
     */
    @Override
    public Optional<Students> getStudentsByCode(String code) {
        logger.debug("Recherche de l'étudiant avec le code : {}", code);
        Optional<Students> student = studentsRepository.findById(code);
        if (student.isPresent()) {
            logger.info("Étudiant trouvé : {}", student.get());
        } else {
            logger.warn("Aucun étudiant trouvé avec le code : {}", code);
        }
        return student;
    }

    /**
     * Recherche un étudiant par son adresse e-mail unique.
     * @param email L'adresse e-mail de l'étudiant.
     * @return Un objet Optional contenant l'étudiant s'il est trouvé.
     */
    @Override
    public Optional<Students> getStudentsByEmail(String email) {
        logger.debug("Recherche de l'étudiant avec l'email : {}", email);
        Optional<Students> student = studentsRepository.findByEmail(email);
        if (student.isPresent()) {
            logger.info("Étudiant trouvé : {}", student.get());
        } else {
            logger.warn("Aucun étudiant trouvé avec l'email : {}", email);
        }
        return student;
    }

    /**
     * Met à jour les informations d'un étudiant existant.
     * @param student L'objet contenant les nouvelles informations.
     * @param code Le code de l'étudiant.
     * @return L'objet étudiant mis à jour.
     */
    @Override
    public Students updateStudents(Students student, String code) {
        logger.info("Mise à jour des informations de l'étudiant avec le code : {}", code);
        Students existingStudent = studentsRepository.findById(code)
                .orElseThrow(() -> {
                    logger.error("Échec de la mise à jour : aucun étudiant trouvé avec le code {}", code);
                    return new RuntimeException("Student does not exist");
                });

        existingStudent.setLastName(student.getLastName());
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setPhone(student.getPhone());
        existingStudent.setSpeciality(student.getSpeciality());
        existingStudent.setEntryAt(student.getEntryAt());
        existingStudent.setFirstDepartureMissionAt(student.getFirstDepartureMissionAt());

        Students updatedStudent = studentsRepository.save(existingStudent);
        logger.info("Étudiant mis à jour avec succès : {}", updatedStudent);
        return updatedStudent;
    }

    /**
     * Supprime un étudiant de la base de données en fonction de son code.
     * @param code Le code de l'étudiant à supprimer.
     */
    @Override
    public void deleteStudentsByCode(String code) {
        logger.warn("Suppression de l'étudiant avec le code : {}", code);
        studentsRepository.deleteById(code);
        logger.info("Étudiant supprimé avec succès.");
    }

    /**
     * Supprime un étudiant de la base de données en fonction de son adresse e-mail.
     */
    @Override
    @Transactional
    public void deleteStudentsByEmail(String email) {
        logger.warn("Suppression de l'étudiant avec l'email : {}", email);
        studentsRepository.deleteByEmail(email);
        logger.info("Étudiant supprimé avec succès.");
    }

}

