package fr.esgi.doctodocapi.model.vo.email;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    private static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public static Email of(String value) {
        if(!validate(value)) {
            throw new WrongEmailFormatException();
        }
        return new Email(value);
    }

    public String getValue() {
        return value;
    }
}
