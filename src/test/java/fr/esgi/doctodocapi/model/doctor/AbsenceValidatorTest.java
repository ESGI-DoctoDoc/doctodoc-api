package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidator;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.OverlappingAbsenceException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbsenceValidatorTest {

    private static final UUID DOCTOR_ID = UUID.randomUUID();
    private static final UUID OTHER_DOCTOR_ID = UUID.randomUUID();

    private Absence createSingleDateAbsence(UUID doctorId, LocalDate date) {
        return Absence.createSingleDate("Ponctuelle", date, doctorId);
    }

    private Absence createRangeAbsence(UUID doctorId, LocalDate start, LocalDate end, LocalTime startHour, LocalTime endHour) {
        return Absence.createWithRange("Plage", start, end, startHour, endHour, doctorId);
    }

    @Test
    void shouldThrowIfTwoSingleDateAbsencesOnSameDay() {
        var existing = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1));

        assertThrows(OverlappingAbsenceException.class, () ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfSingleDateFallsInsideRange() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 5),
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 3));

        assertThrows(OverlappingAbsenceException.class, () ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfExistingSingleDateFallsInsideNewRange() {
        var existing = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 2));
        var toSave = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 3),
                LocalTime.of(8, 0), LocalTime.of(18, 0));

        assertThrows(OverlappingAbsenceException.class, () ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfTwoRangesOverlapInDateAndTime() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 3),
                LocalTime.of(8, 0), LocalTime.of(12, 0));
        var toSave = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 2), LocalDate.of(2025, 7, 4),
                LocalTime.of(10, 0), LocalTime.of(14, 0));

        assertThrows(OverlappingAbsenceException.class, () ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldNotThrowIfDoctorsAreDifferent() {
        var existing = createSingleDateAbsence(OTHER_DOCTOR_ID, LocalDate.of(2025, 7, 1));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1));

        assertDoesNotThrow(() ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldNotThrowIfDatesDoNotOverlap() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 10), LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 5));

        assertDoesNotThrow(() ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldNotThrowIfHoursDoNotOverlapEvenIfDatesDo() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 1),
                LocalTime.of(8, 0), LocalTime.of(10, 0));
        var toSave = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 1),
                LocalTime.of(10, 0), LocalTime.of(12, 0));

        assertDoesNotThrow(() ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowWhenTwoRangesOnSameDayOverlapInHours() {
        var existing = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 16), LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0), LocalTime.of(12, 0));

        var toSave = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 16), LocalDate.of(2025, 7, 16),
                LocalTime.of(10, 0), LocalTime.of(14, 0));

        assertThrows(OverlappingAbsenceException.class,
                () -> AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfSingleDateEqualsRangeStart() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 10), LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 10));

        assertThrows(OverlappingAbsenceException.class,
                () -> AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfSingleDateEqualsRangeEnd() {
        var existing = createRangeAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 10), LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        var toSave = createSingleDateAbsence(DOCTOR_ID, LocalDate.of(2025, 7, 12));

        assertThrows(OverlappingAbsenceException.class,
                () -> AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldThrowIfRangesTouchAndHoursOverlap() {
        var existing = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 14), LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0), LocalTime.of(18, 0));
        var toSave = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 16), LocalDate.of(2025, 7, 18),
                LocalTime.of(8, 0), LocalTime.of(18, 0));

        assertThrows(OverlappingAbsenceException.class,
                () -> AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }

    @Test
    void shouldNotThrowIfRangesTouchButHoursDoNotOverlap() {
        var existing = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 16), LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0), LocalTime.of(10, 0));
        var toSave = createRangeAbsence(DOCTOR_ID,
                LocalDate.of(2025, 7, 16), LocalDate.of(2025, 7, 16),
                LocalTime.of(10, 0), LocalTime.of(12, 0));

        assertDoesNotThrow(() ->
                AbsenceValidator.validateNoConflictWithExisting(toSave, List.of(existing)));
    }
}