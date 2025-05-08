package fr.esgi.doctodocapi.dtos.responses;

import java.util.UUID;

public record OnBoardingPatientResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
