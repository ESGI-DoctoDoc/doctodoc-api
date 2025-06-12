package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link RecurrentSlot} entities.
 * <p>
 * This interface abstracts persistence operations for slot recurrence logic (weekly or monthly).
 */
public interface RecurrentSlotRepository {

    /**
     * Persists a new recurrent slot or updates an existing one.
     *
     * @param recurrentSlot the recurrent slot to save
     * @return the saved or updated recurrent slot
     */
    RecurrentSlot save(RecurrentSlot recurrentSlot);

    /**
     * Retrieves a recurrent slot by its unique identifier.
     *
     * @param id the UUID of the recurrent slot
     * @return an optional containing the found recurrent slot, or empty if not found
     */
    Optional<RecurrentSlot> findById(UUID id);
}
