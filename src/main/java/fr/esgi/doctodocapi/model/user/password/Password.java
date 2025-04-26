package fr.esgi.doctodocapi.model.user.password;

import fr.esgi.doctodocapi.error.exceptions.WrongPasswordFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Password {
    /**
     * au moins 1 caractère spécial, une maj, un chiffre et 6 caractères minimum en tout
     */
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\\-_]).{6,}$");
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

    public static Password fromDatabase(String value) {
        return new Password(value); // pas de validation pour les user déjà inséré (via le populate)
    }

    public String getValue() {
        return value;
    }
}
