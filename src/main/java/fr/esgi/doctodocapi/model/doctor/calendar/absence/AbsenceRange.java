package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.vo.date_range.DateRange;
import fr.esgi.doctodocapi.model.vo.hours_range.HoursRange;

import java.time.LocalDate;
import java.time.LocalTime;

public class AbsenceRange {
    private HoursRange hoursRange;
    private DateRange dateRange;

    public AbsenceRange(HoursRange hoursRange, DateRange dateRange) {
        this.hoursRange = hoursRange;
        this.dateRange = dateRange;
    }

    public static AbsenceRange of(LocalDate start, LocalDate end, LocalTime startHour, LocalTime endHour) {
        return new AbsenceRange(
                HoursRange.of(startHour, endHour),
                DateRange.of(start, end)
        );
    }

    public boolean overlapsWith(AbsenceRange other) {
        return DateRange.isDatesOverlap(this.dateRange, other.dateRange)
                && HoursRange.isTimesOverlap(this.hoursRange, other.hoursRange);
    }

    public HoursRange getHoursRange() {
        return hoursRange;
    }

    public void setHoursRange(HoursRange hoursRange) {
        this.hoursRange = hoursRange;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}