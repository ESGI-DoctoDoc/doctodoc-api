package fr.esgi.doctodocapi.model.user.phone_number;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {
    /**
     * exemple de num qui marche : +330783476669, +33783476669, 0783476669
     */
    private static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^(0[1-9][0-9]{8}|\\+33[0]?[1-9][0-9]{8})$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    private static boolean validate(String phoneNumber) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumber);
        return matcher.matches();
    }

    public static PhoneNumber of(String value) {
        if (!validate(value)) {
            throw new WrongPhoneNumberFormatException();
        }
        return new PhoneNumber(value);
    }

    public String getValue() {
        return value;
    }
}