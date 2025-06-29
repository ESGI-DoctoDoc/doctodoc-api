package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import java.util.UUID;

public record GetDocumentsOfCareTrackingResponse(
        UUID id,
        String name,
        String type,
        String url
) {
}
