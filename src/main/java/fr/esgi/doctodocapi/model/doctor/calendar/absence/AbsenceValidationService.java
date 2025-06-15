package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Utility class providing validation logic to ensure no overlapping absences exist for a doctor.
 * <p>
 * The validation checks for overlaps between ranged absences (with date and hour intervals).
 * If a conflict is detected between the absence to save and any existing ones,
 * an {@link OverlappingAbsenceException} is thrown.
 */
@Service
public final class AbsenceValidationService {

    /**
     * Validates that the absence to be saved does not conflict with any existing absences.
     * <p>
     * Checks for overlap between two ranged absences (on both date and hour ranges).
     *
     * @param toSave the new absence being created or updated
     * @param existingAbsences the list of existing absences to validate against
     * @throws OverlappingAbsenceException if a conflict is found with any existing absence
     */
    public void validateNoConflictWithExisting(Absence toSave, List<Absence> existingAbsences) {
        for (Absence existing : existingAbsences) {
            checkRangeConflict(toSave.getAbsenceRange(), existing.getAbsenceRange());
        }
    }

    private void checkRangeConflict(AbsenceRange range1, AbsenceRange range2) {
        boolean datesOverlap = DateRange.isDatesOverlap(range1.getDateRange(), range2.getDateRange())
                || range1.getDateRange().getStart().isEqual(range2.getDateRange().getEnd())
                || range2.getDateRange().getStart().isEqual(range1.getDateRange().getEnd());

        if (datesOverlap && HoursRange.isTimesOverlap(range1.getHoursRange(), range2.getHoursRange())) {
            throw new OverlappingAbsenceException();
        }
    }
}