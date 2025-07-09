package fr.esgi.doctodocapi.model.vo.date_range;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateRangeTest {
    @Test
    void constructorThrowsWhenStartAfterEnd() {
        LocalDate start = LocalDate.of(2025, 8, 1);
        LocalDate end = LocalDate.of(2025, 7, 31);
        InvalidDateRangeException ex = assertThrows(InvalidDateRangeException.class,
                () -> DateRange.of(start, end));
        assertEquals("date-range.invalid", ex.getCode());
    }

    @Test
    void constructorAcceptsWhenStartEqualsEnd() {
        LocalDate date = LocalDate.of(2025, 7, 15);
        DateRange dr = DateRange.of(date, date);
        assertEquals(date, dr.getStart());
        assertEquals(date, dr.getEnd());
    }

    @Test
    void constructorAcceptsWhenStartBeforeEnd() {
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 7, 31);
        DateRange dr = DateRange.of(start, end);
        assertEquals(start, dr.getStart());
        assertEquals(end, dr.getEnd());
    }
}
