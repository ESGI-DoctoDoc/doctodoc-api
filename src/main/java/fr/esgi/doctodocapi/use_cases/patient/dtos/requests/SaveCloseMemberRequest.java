package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SaveCloseMemberRequest(
        @NotBlank String lastName,
        @NotBlank String firstName,
        @NotNull LocalDate birthdate,
        @NotBlank String email,
        @NotBlank String gender,
        @NotBlank String phoneNumber
) {

    public SaveCloseMemberRequest {
        lastName = lastName.trim();
        firstName = firstName.trim();
        email = email.trim();
        phoneNumber = phoneNumber.trim();
    }
}
