package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.AbsenceValidationService;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.OverlappingAbsenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbsenceValidationServiceTest {

    private AbsenceValidationService validationService;

    @BeforeEach
    void setUp() {
        this.validationService = new AbsenceValidationService();
    }

    private Absence createSingleDayAbsence(LocalDate date) {
        return Absence.createSingleDate("Ponctuelle", date);
    }

    private Absence createRangeAbsence(LocalDate start, LocalDate end, LocalTime startHour, LocalTime endHour) {
        return Absence.createWithRange("Plage", start, end, startHour, endHour);
    }

    @Test
    void shouldThrowIfTwoSingleDayAbsencesOnSameDay() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        Absence existing = createSingleDayAbsence(date);
        Absence toSave = createSingleDayAbsence(date);

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfSingleDayFallsInsideMultiDayRange() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 5),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );
        Absence toSave = createSingleDayAbsence(LocalDate.of(2025, 7, 3));

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfSingleDayEqualsRangeStart() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );
        Absence toSave = createSingleDayAbsence(LocalDate.of(2025, 7, 10));

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfSingleDayEqualsRangeEnd() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );
        Absence toSave = createSingleDayAbsence(LocalDate.of(2025, 7, 12));

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfSingleDayFallsInsideNewMultiDayRange() {
        Absence existing = createSingleDayAbsence(LocalDate.of(2025, 7, 2));
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfTwoRangesOverlapInDateAndTime() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0)
        );
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 2),
                LocalDate.of(2025, 7, 4),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0)
        );

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldNotThrowIfDatesDoNotOverlap() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 10),
                LocalDate.of(2025, 7, 12),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );
        Absence toSave = createSingleDayAbsence(LocalDate.of(2025, 7, 5));

        // Dates 5 juillet vs 10-12 juillet : pas de chevauchement
        assertDoesNotThrow(() ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldNotThrowIfHoursDoNotOverlapEvenIfDatesDo() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 1),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0)
        );
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );

        assertDoesNotThrow(() ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowWhenTwoRangesOnSameDayOverlapInHours() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 16),
                LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0),
                LocalTime.of(12, 0)
        );
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 16),
                LocalDate.of(2025, 7, 16),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0)
        );

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldThrowIfRangesTouchAndHoursOverlap() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 14),
                LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 16),
                LocalDate.of(2025, 7, 18),
                LocalTime.of(8, 0),
                LocalTime.of(18, 0)
        );

        assertThrows(OverlappingAbsenceException.class, () ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }

    @Test
    void shouldNotThrowIfRangesTouchButHoursDoNotOverlap() {
        Absence existing = createRangeAbsence(
                LocalDate.of(2025, 7, 16),
                LocalDate.of(2025, 7, 16),
                LocalTime.of(8, 0),
                LocalTime.of(10, 0)
        );
        Absence toSave = createRangeAbsence(
                LocalDate.of(2025, 7, 16),
                LocalDate.of(2025, 7, 16),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );

        assertDoesNotThrow(() ->
                this.validationService.validateNoConflictWithExisting(toSave, List.of(existing))
        );
    }
}