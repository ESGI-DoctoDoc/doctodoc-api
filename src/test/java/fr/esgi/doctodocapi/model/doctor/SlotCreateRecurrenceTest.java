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

class SlotCreateRecurrenceTest {

    private final LocalDate createdAt = LocalDate.now();
    private final UUID doctorId = UUID.randomUUID();
    private final UUID recurrenceId = UUID.randomUUID();

    private final MedicalConcern concernA = new MedicalConcern(UUID.randomUUID(), "A", 15, List.of(), 10.0, doctorId, createdAt);
    private final MedicalConcern concernB = new MedicalConcern(UUID.randomUUID(), "B", 30, List.of(), 15.0, doctorId, createdAt);
    private final MedicalConcern concernC = new MedicalConcern(UUID.randomUUID(), "C", 20, List.of(), 20.0, doctorId, createdAt);

    private Slot createSlot(LocalDate date, String start, String end, List<MedicalConcern> concerns, UUID recurrenceId) {
        return Slot.createRecurrence(date, LocalTime.parse(start), LocalTime.parse(end), doctorId, concerns, recurrenceId);
    }

    @Test
    void should_create_slot_with_all_expected_fields() {
        LocalDate date = LocalDate.of(2025, 7, 20);
        Slot slot = createSlot(date, "08:00", "10:00", List.of(concernA, concernB), recurrenceId);

        assertAll(
                () -> assertEquals(date, slot.getDate()),
                () -> assertEquals(LocalTime.of(8, 0), slot.getHoursRange().getStart()),
                () -> assertEquals(LocalTime.of(10, 0), slot.getHoursRange().getEnd()),
                () -> assertEquals(doctorId, slot.getDoctorId()),
                () -> assertEquals(recurrenceId, slot.getRecurrenceId()),
                () -> assertEquals(2, slot.getAvailableMedicalConcerns().size()),
                () -> assertTrue(slot.getAvailableMedicalConcerns().containsAll(List.of(concernA, concernB))),
                () -> assertNotNull(slot.getAppointments()),
                () -> assertTrue(slot.getAppointments().isEmpty())
        );
    }

    @Test
    void should_not_throw_when_hours_do_not_overlap_even_with_same_concerns() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 21), "08:00", "09:00", List.of(concernA), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 21), "09:00", "10:00", List.of(concernA), recurrenceId);

        assertDoesNotThrow(() -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_not_throw_when_date_is_different_even_if_times_and_concerns_match() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 20), "08:00", "09:30", List.of(concernA), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 21), "08:00", "09:30", List.of(concernA), recurrenceId);

        assertDoesNotThrow(() -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_not_throw_when_times_overlap_but_no_common_concerns() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 21), "09:00", "11:00", List.of(concernA), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 21), "10:30", "12:00", List.of(concernB), recurrenceId);

        assertDoesNotThrow(() -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_throw_when_times_overlap_and_common_concern_exists() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 21), "09:00", "11:00", List.of(concernA, concernB), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 21), "10:00", "12:00", List.of(concernB, concernC), recurrenceId);

        assertThrows(OverlappingSlotException.class, () -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_throw_when_exact_time_and_exact_concerns() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 22), "08:00", "10:00", List.of(concernA), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 22), "08:00", "10:00", List.of(concernA), recurrenceId);

        assertThrows(OverlappingSlotException.class, () -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_throw_if_any_concern_is_shared_across_multiple_existing_slots() {
        Slot existing1 = createSlot(LocalDate.of(2025, 7, 23), "08:00", "09:00", List.of(concernA), null);
        Slot existing2 = createSlot(LocalDate.of(2025, 7, 23), "09:00", "10:00", List.of(concernB), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 23), "08:30", "09:30", List.of(concernA, concernB), recurrenceId);

        assertThrows(OverlappingSlotException.class, () -> candidate.validateAgainstOverlaps(List.of(existing1, existing2)));
    }

    @Test
    void should_not_throw_if_recurring_slot_has_null_recurrenceId() {
        Slot slot = createSlot(LocalDate.of(2025, 8, 1), "07:00", "08:00", List.of(concernA), null);
        assertNull(slot.getRecurrenceId());
    }

    @Test
    void should_throw_when_overlap_occurs_with_partial_concern_match() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 24), "10:00", "11:00", List.of(concernA), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 24), "10:30", "11:30", List.of(concernA, concernB), recurrenceId);

        assertThrows(OverlappingSlotException.class, () -> candidate.validateAgainstOverlaps(List.of(existing)));
    }

    @Test
    void should_not_throw_when_existing_slot_has_empty_concerns() {
        Slot existing = createSlot(LocalDate.of(2025, 7, 25), "09:00", "10:00", List.of(), null);
        Slot candidate = createSlot(LocalDate.of(2025, 7, 25), "09:30", "10:30", List.of(concernA), recurrenceId);

        assertDoesNotThrow(() -> candidate.validateAgainstOverlaps(List.of(existing)));
    }
}