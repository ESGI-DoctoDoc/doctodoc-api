package fr.esgi.doctodocapi.use_cases.care_tracking.dtos.responses;

import java.util.UUID;

public record GetCareTrackingsResponse(
        UUID id,
        String name,
        String createdAt,
        CareTrackingPatientInfo patient
) {
}
