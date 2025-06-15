package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.doctor.calendar.Calendar;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.OverlappingSlotException;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.password.Password;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DoctorCalendarTest {

    private Doctor doctor;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        doctor = new Doctor(
                UUID.randomUUID(),
                Email.of("doctor@example.com"),
                Password.of("Abdcd76@"),
                PhoneNumber.of("+33612345678"),
                true,
                false,
                null,
                now,
                UUID.randomUUID(),
                null,
                null,
                null,
                false,
                new Calendar(new ArrayList<>(), new ArrayList<>())
        );
    }

    @Test
    void givenValidSlot_whenAddSlot_thenSlotIsAdded() {
        // Given
        Slot slot = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                List.of(),
                null
        );

        // When
        doctor.getCalendar().addSlot(slot);

        // Then
        List<Slot> slots = doctor.getCalendar().getSlots();
        assertEquals(1, slots.size(), "Calendar should contain one slot");
        assertEquals(slot, slots.get(0), "Added slot should match the provided slot");
    }

    @Test
    void givenMultipleSlots_whenAddSlots_thenSlotsAreAddedInOrder() {
        // Given
        Slot slot1 = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                List.of(),
                null
        );
        Slot slot2 = Slot.createRecurrence(
                LocalDate.now().plusDays(2),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                List.of(),
                null
        );

        // When
        doctor.getCalendar().addSlot(slot1);
        doctor.getCalendar().addSlot(slot2);

        // Then
        List<Slot> slots = doctor.getCalendar().getSlots();
        assertEquals(2, slots.size(), "Calendar should contain two slots");
        assertEquals(slot1, slots.get(0), "First slot should be slot1");
        assertEquals(slot2, slots.get(1), "Second slot should be slot2");
    }

    @Test
    void givenOverlappingSlots_whenValidateSlot_thenThrowsOverlappingSlotException() {
        // Given
        LocalDate createdAt = LocalDate.now();
        UUID doctorId = UUID.randomUUID();
        MedicalConcern concern = new MedicalConcern(
                UUID.randomUUID(),
                "General Consultation",
                15,
                List.of(),
                10.0,
                doctorId,
                createdAt
        );
        List<MedicalConcern> concerns = List.of(concern);

        Slot existingSlot = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                concerns,
                null
        );
        Slot overlappingSlot = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 15),
                LocalTime.of(10, 45),
                concerns,
                null
        );

        doctor.getCalendar().addSlot(existingSlot);

        // When & Then
        assertThrows(OverlappingSlotException.class, () ->
                        overlappingSlot.validateAgainstOverlaps(doctor.getCalendar().getSlots()),
                "Overlapping slot should throw OverlappingSlotException");
    }

    @Test
    void givenEmptyCalendar_whenGetSlots_thenReturnsEmptyList() {
        // When
        List<Slot> slots = doctor.getCalendar().getSlots();

        // Then
        assertTrue(slots.isEmpty(), "Calendar should have no slots initially");
    }

    @Test
    void givenDuplicateSlot_whenAddSlot_thenThrowsIllegalStateException() {
        // Given
        Slot slot = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                List.of(),
                null
        );
        doctor.getCalendar().addSlot(slot);

        // When & Then
        assertThrows(IllegalStateException.class, () ->
                        doctor.getCalendar().addSlot(slot),
                "Adding duplicate slot should throw IllegalStateException");
    }

    @Test
    void givenSlotsInRange_whenFilterSlotsByDateRange_thenReturnsMatchingSlots() {
        // Given
        Slot slot1 = Slot.createRecurrence(
                LocalDate.now().plusDays(10),
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                List.of(),
                null
        );
        Slot slot2 = Slot.createRecurrence(
                LocalDate.now().plusDays(20),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                List.of(),
                null
        );
        Slot slot3 = Slot.createRecurrence(
                LocalDate.now().plusDays(25),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                List.of(),
                null
        );

        doctor.getCalendar().addSlot(slot1);
        doctor.getCalendar().addSlot(slot2);
        doctor.getCalendar().addSlot(slot3);

        DateRange range = DateRange.of(LocalDate.now().plusDays(15), LocalDate.now().plusDays(25));

        // When
        List<Slot> filteredSlots = doctor.getCalendar().getSlots().stream()
                .filter(slot -> !slot.getDate().isBefore(range.getStart()) && !slot.getDate().isAfter(range.getEnd()))
                .toList();

        // Then
        assertEquals(2, filteredSlots.size(), "Should return two slots in the date range");
        assertTrue(filteredSlots.contains(slot2), "Should contain slot2");
        assertTrue(filteredSlots.contains(slot3), "Should contain slot3");
    }

    @Test
    void givenNullConcerns_whenCreateSlot_thenSlotIsAdded() {
        // Given
        Slot slot = Slot.createRecurrence(
                LocalDate.now().plusDays(1),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                null,
                null
        );

        // When
        doctor.getCalendar().addSlot(slot);

        // Then
        assertEquals(1, doctor.getCalendar().getSlots().size(), "Calendar should contain one slot with null concerns");
    }


    @Test
    void givenValidAbsence_whenAddAbsence_thenAbsenceIsAdded() {
        Absence absence = Absence.createSingleDate("Congé annuel", LocalDate.now().plusDays(3));
        doctor.getCalendar().addAbsence(absence);

        List<Absence> absences = doctor.getCalendar().getAbsences();
        assertEquals(1, absences.size());
        assertEquals(absence, absences.get(0));
    }

    @Test
    void givenDuplicateAbsence_whenAddAbsence_thenThrowsIllegalStateException() {
        Absence absence = Absence.createSingleDate("Repos", LocalDate.now().plusDays(5));
        doctor.getCalendar().addAbsence(absence);

        assertThrows(IllegalStateException.class, () ->
                doctor.getCalendar().addAbsence(absence));
    }

    @Test
    void givenMultipleAbsences_whenRetrieve_thenCorrectCountReturned() {
        doctor.getCalendar().addAbsence(Absence.createSingleDate("Abs1", LocalDate.now().plusDays(1)));
        doctor.getCalendar().addAbsence(Absence.createSingleDate("Abs2", LocalDate.now().plusDays(2)));
        doctor.getCalendar().addAbsence(Absence.createSingleDate("Abs3", LocalDate.now().plusDays(3)));

        List<Absence> absences = doctor.getCalendar().getAbsences();
        assertEquals(3, absences.size());
    }
}