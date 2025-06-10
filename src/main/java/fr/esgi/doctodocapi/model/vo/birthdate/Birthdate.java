package fr.esgi.doctodocapi.model.vo.birthdate;

import java.time.LocalDate;

public class Birthdate {
    private final LocalDate value;

    private Birthdate(LocalDate value) {
        this.value = value;
    }

    public static Birthdate of(LocalDate value) {
        valid(value);
        return new Birthdate(value);
    }

    private static void valid(LocalDate value) {
        LocalDate now = LocalDate.now();
        if (value.isAfter(now)) {
            throw new InvalidBirthdate();
        }
    }

    public LocalDate getValue() {
        return value;
    }
}
