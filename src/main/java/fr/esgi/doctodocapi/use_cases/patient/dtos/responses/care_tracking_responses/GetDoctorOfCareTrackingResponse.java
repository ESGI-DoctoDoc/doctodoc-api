package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import java.util.UUID;

public record GetDoctorOfCareTrackingResponse(
        UUID id,
        String lastName,
        String firstName,
        String speciality,
        String pictureUrl
) {
}
