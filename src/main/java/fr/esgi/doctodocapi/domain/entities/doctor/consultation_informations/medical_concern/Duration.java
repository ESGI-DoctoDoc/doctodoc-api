package fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern;

public class Duration {
    private final Integer value;

    private Duration(Integer value) {
        this.value = value;
    }

    private static void validate(Integer value) {
        if (value < 0) {
            throw new WrongDurationFormatException();
        }
    }

    public static Duration of(Integer value) {
        validate(value);
        return new Duration(value);
    }

    public Integer getValue() {
        return value;
    }
}
