package fr.esgi.doctodocapi.model.vo.date_range;

import java.time.LocalDate;
import java.util.Objects;

public class DateRange {

    private final LocalDate start;
    private final LocalDate end;

    private DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public static DateRange of(LocalDate start, LocalDate end) {
        validate(start, end);
        return new DateRange(start, end);
    }

    private static void validate(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException();
        }
    }

    public static boolean isDatesOverlap(DateRange range1, DateRange range2) {
        return range1.getStart().isBefore(range2.getEnd()) && range2.getStart().isBefore(range1.getEnd());
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(start, dateRange.start) && Objects.equals(end, dateRange.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}