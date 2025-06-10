package fr.esgi.doctodocapi.model.vo.phone_number;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {
    /**
     * exemple de num qui marche : +33783476669
     */
    private static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^\\+33[1-9]\\d{8}$");

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