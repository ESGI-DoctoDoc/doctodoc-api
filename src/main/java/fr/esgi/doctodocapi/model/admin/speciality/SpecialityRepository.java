package fr.esgi.doctodocapi.model.admin.speciality;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for persistence operations related to {@link Speciality}.
 */
public interface SpecialityRepository {

    /**
     * Saves a new {@link Speciality} or updates an existing one.
     *
     * @param speciality the speciality to save
     * @return the saved or updated speciality
     */
    Speciality save(Speciality speciality);

    /**
     * Retrieves all available specialities.
     *
     * @return a list of all stored {@link Speciality} instances
     */
    List<Speciality> findAll(int page, int size);

    Speciality findByName(String name) throws SpecialityNotFoundException;

    boolean existsByName(String name);

    Speciality findById(UUID id);

    Speciality update(UUID id, String newName);
}
