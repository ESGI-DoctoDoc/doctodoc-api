package fr.esgi.doctodocapi.domain.entities.doctor.calendar;

import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.SlotNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing Slot entities related to doctors' calendars.
 */
public interface SlotRepository {

    /**
     * Retrieves a list of Slots for a given medical concern on a specific date.
     *
     * @param medicalConcernId the UUID identifying the medical concern
     * @param date             the date to filter Slots by
     * @return list of Slots matching the medical concern and date
     * @throws MedicalConcernNotFoundException if the medical concern does not exist
     */
    List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) throws MedicalConcernNotFoundException;

    /**
     * Retrieves a Slot by its unique identifier.
     *
     * @param id the UUID of the Slot
     * @return the Slot entity
     * @throws SlotNotFoundException if no Slot with the given ID exists
     */
    Slot getById(UUID id) throws SlotNotFoundException;
}
