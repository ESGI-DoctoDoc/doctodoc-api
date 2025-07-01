package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document;

import java.util.UUID;

public record GetDocumentForCareTrackingResponse(
        UUID id,
        String name,
        String type,
        String url
) {
}
