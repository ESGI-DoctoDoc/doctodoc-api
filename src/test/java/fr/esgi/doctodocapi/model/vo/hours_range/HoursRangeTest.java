package fr.esgi.doctodocapi.model.vo.hours_range;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class HoursRangeTest {

    @Test
    void should_create_vo_hours_range() {
        LocalTime startHour = LocalTime.of(10, 0);
        LocalTime endHour = LocalTime.of(10, 30);
        HoursRange hoursRangeExpected = HoursRange.of(startHour, endHour);

        assertEquals(hoursRangeExpected, HoursRange.of(startHour, endHour));
    }

    @Test
    void should_throw_exception_if_start_hour_is_after_end_hour() {
        LocalTime startHour = LocalTime.of(11, 0);
        LocalTime endHour = LocalTime.of(10, 30);

        assertThrows(InvalidHoursRangeException.class, () -> HoursRange.of(startHour, endHour));
    }

    @Test
    void should_throw_exception_if_hours_ranges_are_null() {
        assertThrows(IllegalArgumentException.class, () -> HoursRange.of(null, null));
    }

    @Test
    void should_throw_exception_if_first_hour_is_null() {
        LocalTime time = LocalTime.of(10, 0);
        assertThrows(IllegalArgumentException.class, () -> HoursRange.of(null, time));
    }

    @Test
    void should_throw_exception_if_second_hour_is_null() {
        LocalTime time = LocalTime.of(10, 0);
        assertThrows(IllegalArgumentException.class, () -> HoursRange.of(time, null));
    }

    @Test
    void should_return_true_if_hours_ranges_overlap() {
        HoursRange hoursRange1 = HoursRange.of(LocalTime.of(10, 0), LocalTime.of(10, 30));
        HoursRange hoursRange2 = HoursRange.of(LocalTime.of(10, 10), LocalTime.of(11, 0));

        assertTrue(HoursRange.isTimesOverlap(hoursRange1, hoursRange2));
    }

    @Test
    void should_return_false_if_hours_ranges_not_overlap() {
        HoursRange hoursRange1 = HoursRange.of(LocalTime.of(10, 0), LocalTime.of(10, 30));
        HoursRange hoursRange2 = HoursRange.of(LocalTime.of(11, 10), LocalTime.of(11, 10));

        assertFalse(HoursRange.isTimesOverlap(hoursRange1, hoursRange2));
    }

    @Test
    void should_return_false_if_hours_ranges_not_overlap_even_if_first_hour_range_start_and_second_hour_range_end_is_the_same() {
        HoursRange hoursRange1 = HoursRange.of(LocalTime.of(10, 0), LocalTime.of(10, 30));
        HoursRange hoursRange2 = HoursRange.of(LocalTime.of(10, 30), LocalTime.of(11, 10));

        assertFalse(HoursRange.isTimesOverlap(hoursRange1, hoursRange2));
    }


}