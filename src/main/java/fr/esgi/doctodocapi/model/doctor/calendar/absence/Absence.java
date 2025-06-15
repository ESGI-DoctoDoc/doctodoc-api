package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model representing an absence period for a doctor.
 * <p>
 * An absence can either be:
 * <ul>
 *   <li>a single day off (with a specific date)</li>
 *   <li>a period with a start/end date and time range (represented by {@link AbsenceRange})</li>
 * </ul>
 */
public class Absence {
    UUID id;
    String description;
    LocalDate date;
    AbsenceRange absenceRange;
    LocalDate createdAt;

    public Absence(UUID id, String description, LocalDate date, AbsenceRange absenceRange, LocalDate createdAt) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.absenceRange = absenceRange;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create a single-day absence.
     *
     * @param description Description of the absence
     * @param date        The day the doctor is unavailable
     * @return A new Absence instance representing a single-day absence
     */
    public static Absence createSingleDate(String description, LocalDate date) {
        return new Absence(
                UUID.randomUUID(),
                description,
                date,
                null,
                LocalDate.now()
        );
    }

    /**
     * Factory method to create a multi-day absence with specific hours.
     *
     * @param description Description of the absence
     * @param startDate   Start date of the absence period
     * @param endDate     End date of the absence period
     * @param startHour   Start hour each day
     * @param endHour     End hour each day
     * @return A new Absence instance representing a ranged absence
     */
    public static Absence createWithRange(String description, LocalDate startDate, LocalDate endDate, LocalTime startHour, LocalTime endHour) {
        AbsenceRange absenceRange = AbsenceRange.of(startDate, endDate, startHour, endHour);

        return new Absence(
                UUID.randomUUID(),
                description,
                null,
                absenceRange,
                LocalDate.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public AbsenceRange getAbsenceRange() {
        return absenceRange;
    }

    public void setAbsenceRange(AbsenceRange absenceRange) {
        this.absenceRange = absenceRange;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Absence absence = (Absence) o;
        return Objects.equals(id, absence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


