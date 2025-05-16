package fr.esgi.doctodocapi.model.vo.rpps;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rpps {
    private static final Pattern VALID_RPPS_REGEX = Pattern.compile("^\\d{11}$");

    private final String value;

    private Rpps(String value) {
        this.value = value;
    }

    private static boolean validate(String rpps) {
        Matcher matcher = VALID_RPPS_REGEX.matcher(rpps);
        return matcher.matches();
    }

    public static Rpps of(String value) {
        if (!validate(value)) {
            throw new WrongRppsFormatException();
        }
        return new Rpps(value);
    }

    public String getValue() {
        return value;
    }
}