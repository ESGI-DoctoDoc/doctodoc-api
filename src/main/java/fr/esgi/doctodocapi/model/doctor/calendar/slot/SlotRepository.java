package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;

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

    /**
     * Saves a list of slots (typically batch insertion for recurrences).
     *
     * @param slots the slots to save
     * @param doctorId id of the doctor
     * @return the saved slots
     */
    List<Slot> saveAll(List<Slot> slots, UUID doctorId);

    /**
     * Retrieves all slots for a given doctor starting from a specified date, ordered by date in ascending order.
     *
     * @param doctorId the UUID of the doctor
     * @param startDate the start date from which to retrieve slots (inclusive)
     * @return list of slots for the doctor from the start date onward
     */
    List<Slot> findAllByDoctorIdAndDateAfter(UUID doctorId, LocalDate startDate);

    Slot findOneByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date);

    List<Slot> findVisibleByDoctorIdAndDateAfter(UUID doctorId, LocalDate startDate, List<String> validStatuses, int page, int size);

    List<Slot> findVisibleByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, List<String> validStatuses, int page, int size);

    List<Slot> getByNextDateAndMedicalConcernId(LocalDate date, UUID doctorId);

    List<Slot> getByPreviousDateAndMedicalConcernId(LocalDate date, UUID doctorId);
}
