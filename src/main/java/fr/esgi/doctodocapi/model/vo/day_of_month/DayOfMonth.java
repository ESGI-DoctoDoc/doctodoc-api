package fr.esgi.doctodocapi.model.vo.day_of_month;

import java.util.Objects;

public class DayOfMonth {
    private final int day;

    public DayOfMonth(int day) {
        if (day < 1 || day > 31) {
            throw new InvalidDayOfMonthException();
        }
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DayOfMonth that = (DayOfMonth) o;
        return day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(day);
    }
}
