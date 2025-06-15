package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.doctor.calendar.slot.OverlappingSlotException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SlotRecurrenceTest {

    private final LocalDate createdAt = LocalDate.now();
    private final UUID doctorId = UUID.randomUUID();
    private final UUID recurrenceWeeklyId = UUID.randomUUID();
    private final UUID recurrenceMonthlyId = UUID.randomUUID();

    private final MedicalConcern concernA = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0, doctorId, createdAt);
    private final MedicalConcern concernB = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0, doctorId, createdAt);

    private Slot createRecurrence(LocalDate date, String start, String end, List<MedicalConcern> concerns, UUID recurrenceId) {
        return Slot.createRecurrence(date, LocalTime.parse(start), LocalTime.parse(end), concerns, recurrenceId);
    }

    @Test
    void should_create_weekly_slot_with_valid_recurrenceId() {
        Slot weeklySlot = createRecurrence(LocalDate.of(2025, 6, 10), "08:00", "09:00", List.of(concernA), recurrenceWeeklyId);

        assertEquals(recurrenceWeeklyId, weeklySlot.getRecurrenceId());
        assertEquals(LocalTime.of(8, 0), weeklySlot.getHoursRange().getStart());
        assertEquals(LocalTime.of(9, 0), weeklySlot.getHoursRange().getEnd());
        assertEquals(List.of(concernA), weeklySlot.getAvailableMedicalConcerns());
    }

    @Test
    void should_create_monthly_slot_with_valid_recurrenceId() {
        Slot monthlySlot = createRecurrence(LocalDate.of(2025, 6, 15), "10:00", "11:00", List.of(concernB), recurrenceMonthlyId);

        assertEquals(recurrenceMonthlyId, monthlySlot.getRecurrenceId());
        assertEquals(LocalTime.of(10, 0), monthlySlot.getHoursRange().getStart());
        assertEquals(LocalTime.of(11, 0), monthlySlot.getHoursRange().getEnd());
        assertEquals(List.of(concernB), monthlySlot.getAvailableMedicalConcerns());
    }

    @Test
    void should_not_throw_when_weekly_and_monthly_slots_do_not_overlap() {
        Slot weekly = createRecurrence(LocalDate.of(2025, 6, 3), "08:00", "09:00", List.of(concernA), recurrenceWeeklyId);
        Slot monthly = createRecurrence(LocalDate.of(2025, 6, 3), "09:00", "10:00", List.of(concernA), recurrenceMonthlyId);

        assertDoesNotThrow(() -> weekly.validateAgainstOverlaps(List.of(monthly)));
        assertDoesNotThrow(() -> monthly.validateAgainstOverlaps(List.of(weekly)));
    }

    @Test
    void should_throw_when_weekly_and_monthly_slots_overlap_and_have_common_concern() {
        Slot weekly = createRecurrence(LocalDate.of(2025, 6, 10), "08:00", "09:30", List.of(concernA), recurrenceWeeklyId);
        Slot monthly = createRecurrence(LocalDate.of(2025, 6, 10), "09:00", "10:00", List.of(concernA), recurrenceMonthlyId);

        assertThrows(OverlappingSlotException.class, () -> monthly.validateAgainstOverlaps(List.of(weekly)));
    }

    @Test
    void should_not_throw_when_recurrences_overlap_but_have_no_common_concern() {
        Slot weekly = createRecurrence(LocalDate.of(2025, 6, 17), "08:00", "09:30", List.of(concernA), recurrenceWeeklyId);
        Slot monthly = createRecurrence(LocalDate.of(2025, 6, 17), "09:00", "10:00", List.of(concernB), recurrenceMonthlyId);

        assertDoesNotThrow(() -> monthly.validateAgainstOverlaps(List.of(weekly)));
    }

    @Test
    void should_support_multiple_slots_same_recurrenceId() {
        Slot slot1 = createRecurrence(LocalDate.of(2025, 6, 3), "08:00", "09:00", List.of(concernA), recurrenceWeeklyId);
        Slot slot2 = createRecurrence(LocalDate.of(2025, 6, 10), "08:00", "09:00", List.of(concernA), recurrenceWeeklyId);
        Slot slot3 = createRecurrence(LocalDate.of(2025, 6, 17), "08:00", "09:00", List.of(concernA), recurrenceWeeklyId);

        assertEquals(recurrenceWeeklyId, slot1.getRecurrenceId());
        assertEquals(recurrenceWeeklyId, slot2.getRecurrenceId());
        assertEquals(recurrenceWeeklyId, slot3.getRecurrenceId());
    }

    @Test
    void should_throw_if_slots_from_same_recurrence_overlap_each_other_with_common_concern() {
        Slot slot1 = createRecurrence(LocalDate.of(2025, 6, 3), "08:00", "09:30", List.of(concernA), recurrenceWeeklyId);
        Slot slot2 = createRecurrence(LocalDate.of(2025, 6, 3), "09:00", "10:00", List.of(concernA), recurrenceWeeklyId);

        assertThrows(OverlappingSlotException.class, () -> slot2.validateAgainstOverlaps(List.of(slot1)));
    }
}