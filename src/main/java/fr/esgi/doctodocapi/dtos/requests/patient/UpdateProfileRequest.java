package fr.esgi.doctodocapi.dtos.requests.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @NotBlank String gender,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate birthdate

) {
    public UpdateProfileRequest {
        gender = gender.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
    }
}
