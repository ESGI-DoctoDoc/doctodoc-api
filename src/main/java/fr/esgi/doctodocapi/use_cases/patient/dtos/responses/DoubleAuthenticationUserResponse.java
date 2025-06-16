package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import java.util.UUID;

public record DoubleAuthenticationUserResponse(
        UUID id, // can be doctorId ou patientId
        String token,
        boolean hasOnBoardingDone,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
