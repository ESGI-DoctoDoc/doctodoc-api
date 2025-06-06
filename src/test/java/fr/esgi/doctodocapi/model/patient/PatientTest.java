package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.samples.UserSample;
import fr.esgi.doctodocapi.model.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatientTest {
    @Test
    void should_create_valid_close_member_patient() {
        User user = UserSample.get();

        String firstName = "firstName";
        String lastName = "lastName";
        String email = "email@email.com";
        String phoneNumber = "+33102030405";
        String gender = "MALE";
        LocalDate birthdate = LocalDate.of(2016, 7, 1);

        Patient result = Patient.createCloseMember(user, firstName, lastName, birthdate, email, gender, phoneNumber);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(email, result.getEmail().getValue());
        assertEquals(phoneNumber, result.getPhoneNumber().getValue());
        assertEquals(gender, result.getGender().name());
        assertEquals(birthdate, result.getBirthdate().getValue());

    }

}