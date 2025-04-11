package com.springbootTP.springbootPostgreSQL.backend.service;

import com.springbootTP.springbootPostgreSQL.backend.model.Students;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour gérer les opérations CRUD sur les étudiants.
 */
public interface IStudentsService {

    /**
     * Sauvegarde un nouvel étudiant ou met à jour un étudiant existant.
     * @param student L'entité de l'étudiant à sauvegarder.
     * @return L'étudiant sauvegardé avec son identifiant généré.
     */
    Students saveStudents(Students student);

    /**
     * Récupère la liste de tous les étudiants enregistrés dans la base de données.
     * @return Une liste contenant tous les étudiants.
     */
    List<Students> getAllStudents();

    /**
     * Recherche un étudiant par son code unique.
     * @param code Le code de l'étudiant recherché.
     * @return Un objet Optional contenant l'étudiant s'il existe, sinon vide.
     */
    Optional<Students> getStudentsByCode(String code);

    /**
     * Recherche un étudiant par son adresse email unique.
     * @param email L'email de l'étudiant recherché.
     * @return Un objet Optional contenant l'étudiant s'il existe, sinon vide.
     */
    Optional<Students> getStudentsByEmail(String email);

    /**
     * Met à jour les informations d'un étudiant existant.
     * @param student L'entité de l'étudiant avec les nouvelles données.
     * @return L'étudiant mis à jour.
     */
    Students updateStudents(Students student, String code);

    /**
     * Supprime un étudiant en fonction de son code unique.
     * @param code Le code de l'étudiant à supprimer.
     */
    void deleteStudentsByCode(String code);

    /**
     * Supprime un étudiant en fonction de son adresse email unique
     * * @param email L'email de l'étudiant à supprimer.
     */
    void deleteStudentsByEmail(String students);


}
