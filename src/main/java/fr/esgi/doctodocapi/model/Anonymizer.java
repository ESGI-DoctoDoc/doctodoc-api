package fr.esgi.doctodocapi.model;

import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.vo.email.Email;
import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;

import java.util.UUID;

public class Anonymizer {
    private Anonymizer() {
    }

    public static void anonymize(User user) {
        user.setEmail(anonymizeEmail());
        user.setPhoneNumber(anonymizePhone());
    }

    private static Email anonymizeEmail() {
        return Email.of("user" + UUID.randomUUID().toString().substring(0, 6) + "@example.com");
    }

    private static PhoneNumber anonymizePhone() {
        int firstDigit = 9;
        int rest = 100_000_000 + (int) (Math.random() * 900_000_000);
        String phone = "+33" + firstDigit + String.valueOf(rest).substring(1);
        return PhoneNumber.of(phone);
    }
}
