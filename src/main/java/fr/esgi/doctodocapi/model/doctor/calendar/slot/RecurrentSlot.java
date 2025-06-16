package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a recurrence rule for generating multiple slots over time.
 * <p>
 * A {@code RecurrentSlot} defines a temporal recurrence (weekly or monthly) over a start and end date.
 * It is used to link generated {@link Slot} instances to a common recurrence group.
 */
public class RecurrentSlot {
    private UUID id;
    private RecurrenceType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public RecurrentSlot(UUID id, RecurrenceType type, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }

    /**
     * Creates a new weekly recurrence instance.
     *
     * @param startDate the start date of the recurrence
     * @param endDate   the end date of the recurrence
     * @return a new {@code RecurrentSlot} instance with type WEEKLY
     */
    public static RecurrentSlot createWeekly(LocalDate startDate, LocalDate endDate) {
        return new RecurrentSlot(UUID.randomUUID(), RecurrenceType.WEEKLY, startDate, endDate, LocalDateTime.now());
    }

    /**
     * Creates a new monthly recurrence instance.
     *
     * @param startDate the start date of the recurrence
     * @param endDate   the end date of the recurrence
     * @return a new {@code RecurrentSlot} instance with type MONTHLY
     */
    public static RecurrentSlot createMonthly(LocalDate startDate, LocalDate endDate) {
        return new RecurrentSlot(UUID.randomUUID(), RecurrenceType.MONTHLY, startDate, endDate, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RecurrenceType getType() {
        return type;
    }

    public void setType(RecurrenceType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecurrentSlot that = (RecurrentSlot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}