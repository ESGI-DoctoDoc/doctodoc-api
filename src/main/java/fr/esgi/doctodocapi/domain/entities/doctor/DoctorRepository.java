package fr.esgi.doctodocapi.domain.entities.doctor;

import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.DoctorNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Doctor persistence operations.
 */
public interface DoctorRepository {

    /**
     * Saves or updates a Doctor entity.
     *
     * @param doctor the Doctor to save
     */
    void save(Doctor doctor);

    /**
     * Finds a Doctor by the associated User ID.
     *
     * @param doctorId the UUID of the User linked to the Doctor
     * @return the Doctor entity, or null if not found
     */
    Doctor findDoctorByUserId(UUID doctorId);

    /**
     * Retrieves a Doctor by their Doctor ID.
     *
     * @param id the UUID of the Doctor
     * @return the Doctor entity
     * @throws DoctorNotFoundException if no Doctor with the given ID exists
     */
    Doctor getById(UUID id) throws DoctorNotFoundException;

    /**
     * Checks if a Doctor exists by the given Doctor ID.
     *
     * @param doctorId the UUID of the Doctor
     * @return true if the Doctor exists, false otherwise
     */
    boolean isExistsById(UUID doctorId);

    /**
     * Retrieves a Doctor by the associated User ID, wrapped in Optional.
     *
     * @param id the UUID of the User linked to the Doctor
     * @return Optional containing the Doctor if found, empty otherwise
     */
    Optional<Doctor> getByUserId(UUID id);

    List<Doctor> searchDoctors(String value, String speciality, List<String> lowercaseLanguages, int page, int size);
}
