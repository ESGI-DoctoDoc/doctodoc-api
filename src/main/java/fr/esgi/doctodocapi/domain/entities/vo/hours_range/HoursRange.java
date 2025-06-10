package fr.esgi.doctodocapi.domain.entities.vo.hours_range;

import java.time.LocalTime;
import java.util.Objects;

public class HoursRange {

    private final LocalTime start;
    private final LocalTime end;

    private HoursRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public static HoursRange of(LocalTime start, LocalTime end) {
        validate(start, end);
        return new HoursRange(start, end);
    }

    public static boolean isTimesOverlap(HoursRange range1, HoursRange range2) {
        return range1.getStart().isBefore(range2.getEnd()) && range2.getStart().isBefore(range1.getEnd());
    }

    private static void validate(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        if (start.isAfter(end)) {
            throw new InvalidHoursRangeException();
        }
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HoursRange that = (HoursRange) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
