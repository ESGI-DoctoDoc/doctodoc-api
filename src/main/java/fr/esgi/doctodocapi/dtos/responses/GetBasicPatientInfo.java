package fr.esgi.doctodocapi.dtos.responses;

import java.util.UUID;

public record GetBasicPatientInfo(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
