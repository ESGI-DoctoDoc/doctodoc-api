package fr.esgi.doctodocapi.model.doctor.professionnal_informations;

public class Rpps {
    private final String value;

    private Rpps(String value) {
        this.value = value;
    }

    public static Rpps of(String value) {
        return new Rpps(value);
    }

    public String getValue() {
        return value;
    }
}
