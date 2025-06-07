package fr.esgi.doctodocapi.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProfileResponse(
        UUID id,
        String gender,
        String firstName,
        String lastName,
        LocalDate birthdate,
        String email,
        String phoneNumber
) {
}
