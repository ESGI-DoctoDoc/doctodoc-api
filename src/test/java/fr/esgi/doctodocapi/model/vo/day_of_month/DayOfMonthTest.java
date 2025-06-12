package fr.esgi.doctodocapi.model.vo.day_of_month;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DayOfMonthTest {
    @Test
    void constructorThrowsIfZero() {
        assertThrows(InvalidDayOfMonthException.class, () -> new DayOfMonth(0));
    }
    @Test
    void constructorThrowsIfTooLarge() {
        assertThrows(InvalidDayOfMonthException.class, () -> new DayOfMonth(32));
    }
    @Test
    void constructorAcceptsValid() {
        DayOfMonth dom = new DayOfMonth(15);
        assertEquals(15, dom.getDay());
    }
}
