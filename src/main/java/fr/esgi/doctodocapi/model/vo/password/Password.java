package fr.esgi.doctodocapi.model.vo.password;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    /**
     * au moins 1 caractère spécial (tout ce qui n’est ni lettre ni chiffre), une maj, un chiffre et 6 caractères minimum en tout
     */
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{6,}$");

    private final String value;

    private Password(String value) {
        this.value = value;
    }

    private static boolean validate(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    public static Password of(String value) {
        if (!validate(value)){
            throw new WrongPasswordFormatException();
        }
        return new Password(value);
    }

    public String getValue() {
        return value;
    }
}
