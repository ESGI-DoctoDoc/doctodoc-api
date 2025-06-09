package fr.esgi.doctodocapi.dtos.responses;

import java.util.UUID;

public record GetSearchDoctorResponse(
        UUID id,
        String firstName,
        String lastName,
        String speciality
) {
}
