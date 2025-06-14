package fr.esgi.doctodocapi.model.doctor.calendar.slot;


import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import fr.esgi.doctodocapi.use_cases.patient.appointment.MedicalConcernNotAuthorizedException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a single availability slot in a doctor's calendar.
 * <p>
 * A {@code Slot} has a date, a range of hours, and is linked to a doctor. It can be standalone
 * or part of a recurrence series via {@code recurrenceId}. Each slot may be associated with one or more
 * {@link MedicalConcern}, and can hold multiple {@link Appointment}.
 */
public class Slot {
    private UUID id;
    private LocalDate date;
    private HoursRange hoursRange;
    private UUID recurrenceId;
    private UUID doctorId;
    private List<Appointment> appointments;
    private List<MedicalConcern> availableMedicalConcerns;

    // basic info
    public Slot(UUID id, LocalDate date, LocalTime startHour, LocalTime endHour) {
        this.id = id;
        this.date = date;
        this.hoursRange = HoursRange.of(startHour, endHour);
    }

    public Slot(UUID id, LocalDate date, LocalTime startHour, LocalTime endHour, List<Appointment> appointments, List<MedicalConcern> availableMedicalConcerns, UUID doctorId, UUID recurrenceId) {
        this.id = id;
        this.date = date;
        this.hoursRange = HoursRange.of(startHour, endHour);
        this.appointments = appointments;
        this.availableMedicalConcerns = availableMedicalConcerns;
        this.doctorId = doctorId;
        this.recurrenceId = recurrenceId;
    }


    /**
     * Validates that the current slot does not overlap with other existing slots
     * for the same date and at least one common medical concern.
     *
     * @param existingSlots list of slots to check against
     * @throws OverlappingSlotException if overlap is detected
     */
    public void validateAgainstOverlaps(List<Slot> existingSlots) {
        for(Slot existingSlot : existingSlots) {
            if (HoursRange.isTimesOverlap(this.hoursRange, existingSlot.getHoursRange()) && this.date.equals(existingSlot.getDate())) {
                boolean hasCommonConcern = this.getAvailableMedicalConcerns().stream().anyMatch(thisConcern -> existingSlot.getAvailableMedicalConcerns().contains(thisConcern));
                if(hasCommonConcern) {
                    throw new OverlappingSlotException();
                }
            }
        }
    }

    /**
     * Validates that a medical concern is allowed for this slot.
     *
     * @param medicalConcern the concern to verify
     * @throws MedicalConcernNotAuthorizedException if the concern is not authorized in this slot
     */
    public void validateIfSlotIsAuthorized(MedicalConcern medicalConcern) {
        if (!this.getAvailableMedicalConcerns().contains(medicalConcern)) {
            throw new MedicalConcernNotAuthorizedException();
        }
    }

    /**
     * Factory method for creating a standalone slot (non-recurrent).
     *
     * @param date             date of the slot
     * @param startHour        start hour
     * @param endHour          end hour
     * @param doctorId         ID of the doctor
     * @param medicalConcerns  allowed medical concerns
     * @return a new {@code Slot} instance
     */
    public static Slot create(LocalDate date, LocalTime startHour, LocalTime endHour, UUID doctorId, List<MedicalConcern> medicalConcerns) {
        return new Slot(
                UUID.randomUUID(),
                date,
                startHour,
                endHour,
                new ArrayList<>(),
                medicalConcerns,
                doctorId,
                null
        );
    }

    /**
     * Factory method for creating a slot that is part of a recurrence.
     *
     * @param date             date of the slot
     * @param startHour        start hour
     * @param endHour          end hour
     * @param doctorId         ID of the doctor
     * @param medicalConcerns  allowed medical concerns
     * @param recurrenceId     ID of the recurrence series
     * @return a new {@code Slot} instance with a recurrence ID
     */
    public static Slot createRecurrence(LocalDate date, LocalTime startHour, LocalTime endHour, UUID doctorId, List<MedicalConcern> medicalConcerns, UUID recurrenceId) {
        return new Slot(
                UUID.randomUUID(),
                date,
                startHour,
                endHour,
                new ArrayList<>(),
                medicalConcerns,
                doctorId,
                recurrenceId
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public HoursRange getHoursRange() {
        return hoursRange;
    }

    public void setHoursRange(HoursRange hoursRange) {
        this.hoursRange = hoursRange;
    }

    public List<MedicalConcern> getAvailableMedicalConcerns() {
        return availableMedicalConcerns;
    }

    public void setAvailableMedicalConcerns(List<MedicalConcern> availableMedicalConcerns) {
        this.availableMedicalConcerns = availableMedicalConcerns;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public UUID getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(UUID recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return Objects.equals(id, slot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
