package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO representing a request to update a patient's profile.
 * Contains personal information fields that are required for the update.
 *
 * @param gender    The gender of the patient (non-blank)
 * @param firstName The first name of the patient (non-blank)
 * @param lastName  The last name of the patient (non-blank)
 * @param birthdate The birthdate of the patient (non-null)
 */
public record UpdateProfileRequest(
        @NotBlank String gender,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthdate

) {
    /**
     * Trims all string fields to remove leading and trailing whitespaces.
     */
    public UpdateProfileRequest {
        gender = gender.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
    }
}
