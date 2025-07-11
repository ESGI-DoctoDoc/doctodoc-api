package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import java.time.LocalDate;
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
    Absence save(Absence absence, UUID doctorId);

    /**
     * Retrieves all absences registered for a given doctor.
     *
     * @param doctorId the UUID of the doctor
     * @return a list of {@link Absence} entities associated with the specified doctor
     */
    List<Absence> findAllByDoctorId(UUID doctorId);

    /**
     * Deletes an absence by its ID by marking it as deleted (soft delete).
     *
     * @param absenceId the UUID of the absence to delete
     */
    void delete(UUID absenceId);

    /**
     * Retrieves an absence by its ID, excluding soft-deleted entries.
     *
     * @param absenceId the UUID of the absence to retrieve
     * @return the corresponding {@link Absence}
     * @throws AbsenceNotFoundException if no matching absence is found
     */
    Absence findById(UUID absenceId);

    List<Absence> findAllByDoctorIdAndDate(UUID doctorId, LocalDate date);

    List<Absence> findAllByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, int page, int size);

    List<Absence> findAllByDoctorIdAndStartDateAfterNow(UUID doctorId, LocalDate date, int page, int size);

    Absence update(Absence absence);
}
