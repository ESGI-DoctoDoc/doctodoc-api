package fr.esgi.doctodocapi.dtos.requests.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PatientOnBoardingRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotNull
        LocalDate birthdate,
        UUID doctorId
) {
}
