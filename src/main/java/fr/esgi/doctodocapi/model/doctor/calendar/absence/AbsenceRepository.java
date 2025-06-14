package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Absence} domain entities.
 * <p>
 * Provides methods to persist and retrieve doctor's absence records.
 */
public interface AbsenceRepository {

    /**
     * Saves an {@link Absence} entity to the underlying persistence layer.
     * <p>
     * This can be used for both creating a new absence and updating an existing one.
     *
     * @param absence the absence entity to save
     * @return the persisted absence, possibly with a generated ID
     */
    Absence save(Absence absence);

    /**
     * Retrieves all absences registered for a given doctor.
     *
     * @param doctorId the UUID of the doctor
     * @return a list of {@link Absence} entities associated with the specified doctor
     */
    List<Absence> findAllByDoctorId(UUID doctorId);
}
