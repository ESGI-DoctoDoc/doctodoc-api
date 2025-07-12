package fr.esgi.doctodocapi.model;

import fr.esgi.doctodocapi.model.patient.Patient;
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

    public static void anonymize(Patient patient) {
        patient.setFirstName(anonymizeFirstName());
        patient.setLastName(anonymizeName());
        patient.setPhoneNumber(anonymizePhone());
        patient.setEmail(anonymizeEmail());
        patient.setDoctor(null);
    }

    private static String anonymizeName() {
        return "NOM" + UUID.randomUUID().toString().substring(0, 6);
    }

    private static String anonymizeFirstName() {
        return "PRE" + UUID.randomUUID().toString().substring(0, 6);
    }

    private static Email anonymizeEmail() {
        return Email.of("user" + UUID.randomUUID().toString().substring(0, 6) + "@example.com");
    }

    private static PhoneNumber anonymizePhone() {
        return PhoneNumber.of("+3360000" + (int) (Math.random() * 10000));
    }
}
