package fr.esgi.doctodocapi.model.patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Patient persistence operations.
 */
public interface PatientRepository {

    /**
     * Retrieves a patient by their associated user ID.
     *
     * @param userId the UUID of the user
     * @return an Optional containing the Patient if found, or empty otherwise
     */
    Optional<Patient> getByUserId(UUID userId);

    /**
     * Saves or updates the given patient.
     *
     * @param patient the Patient entity to save
     */
    Patient save(Patient patient);

    /**
     * Checks if a main patient account exists for the given user ID.
     *
     * @param userId the UUID of the user
     * @return true if a main account exists, false otherwise
     */
    boolean isExistMainAccount(UUID userId);

    /**
     * Retrieves related patients for the given patient ID.
     *
     * @param id the UUID of the patient
     * @return list of related Patient entities
     */
    List<Patient> getCloseMembers(UUID id);

    /**
     * Retrieves a patient by their patient ID.
     *
     * @param id the UUID of the patient
     * @return the Patient entity
     * @throws PatientNotFoundException if no patient with the ID is found
     */
    Patient getById(UUID id) throws PatientNotFoundException;

    /**
     * Updates the given patient entity.
     *
     * @param patient the Patient to update
     * @return the updated Patient entity
     */
    Patient update(Patient patient);

    void delete(UUID patient);
}
