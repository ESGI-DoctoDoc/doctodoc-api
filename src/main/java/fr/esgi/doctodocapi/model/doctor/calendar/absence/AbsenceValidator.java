package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.util.List;

/**
 * Utility class providing validation logic to ensure no overlapping absences exist for a doctor.
 * <p>
 * The validation supports both single-date absences and ranged absences (with date and hour intervals).
 * If a conflict is detected between the absence to save and any of the existing ones,
 * an {@link OverlappingAbsenceException} is thrown.
 */
public final class AbsenceValidator {

    private AbsenceValidator() {}

    /**
     * Validates that the absence to be saved does not conflict with any existing absences.
     * <p>
     * Checks for overlap:
     * <ul>
     *     <li>Between two single-date absences</li>
     *     <li>Between a single-date and a ranged absence</li>
     *     <li>Between two ranged absences (on both date and hour ranges)</li>
     * </ul>
     *
     * @param toSave the new absence being created or updated
     * @param existingAbsences the list of existing absences to validate against
     * @throws OverlappingAbsenceException if a conflict is found with any existing absence
     */
    public static void validateNoConflictWithExisting(Absence toSave, List<Absence> existingAbsences) {
        for (Absence existing : existingAbsences) {
            if (isSingleDate(toSave) && isSingleDate(existing)) {
                checkSameDateConflict(toSave.getDate(), existing.getDate());
            }

            if (isSingleDate(toSave) && isRange(existing)) {
                checkDateInRangeConflict(toSave.getDate(), existing.getAbsenceRange().getDateRange());
            }

            if (isRange(toSave) && isSingleDate(existing)) {
                checkDateInRangeConflict(existing.getDate(), toSave.getAbsenceRange().getDateRange());
            }

            if (isRange(toSave) && isRange(existing)) {
                checkRangeConflict(toSave.getAbsenceRange(), existing.getAbsenceRange());
            }
        }
    }

    private static boolean isSingleDate(Absence absence) {
        return absence.getDate() != null;
    }

    private static boolean isRange(Absence absence) {
        return absence.getAbsenceRange() != null;
    }

    private static void checkSameDateConflict(LocalDate date1, LocalDate date2) {
        if (date1.isEqual(date2)) {
            throw new OverlappingAbsenceException();
        }
    }

    private static void checkDateInRangeConflict(LocalDate date, DateRange range) {
        if (!date.isBefore(range.getStart()) && !date.isAfter(range.getEnd())) {
            throw new OverlappingAbsenceException();
        }
    }

    private static void checkRangeConflict(AbsenceRange range1, AbsenceRange range2) {
        boolean datesOverlap =
                DateRange.isDatesOverlap(
                        range1.getDateRange(), range2.getDateRange())
                        || range1.getDateRange().getStart().isEqual(range2.getDateRange().getEnd())
                        || range2.getDateRange().getStart().isEqual(range1.getDateRange().getEnd()
                );

        if (datesOverlap && HoursRange.isTimesOverlap(range1.getHoursRange(), range2.getHoursRange())) {
            throw new OverlappingAbsenceException();
        }
    }
}