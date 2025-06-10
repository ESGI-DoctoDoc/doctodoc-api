package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import java.util.UUID;

public record GetSearchDoctorResponse(
        UUID id,
        String firstName,
        String lastName,
        String speciality,
        String pictureUrl
) {
}
