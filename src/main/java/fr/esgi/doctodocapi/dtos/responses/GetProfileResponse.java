package fr.esgi.doctodocapi.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO representing a patient's profile information returned by the system.
 *
 * @param id          Unique identifier of the patient
 * @param gender      Gender of the patient
 * @param firstName   First name of the patient
 * @param lastName    Last name of the patient
 * @param birthdate   Birthdate of the patient
 * @param email       Email address associated with the patient's account
 * @param phoneNumber Phone number associated with the patient's account
 */
public record GetProfileResponse(
        UUID id,
        String gender,
        String firstName,
        String lastName,
        LocalDate birthdate,
        String email,
        String phoneNumber
) {
}
