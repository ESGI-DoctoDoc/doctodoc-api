package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document;

import java.util.UUID;

public record GetDocumentForCareTrackingResponse(
        UUID id,
        String name,
        String type,
        String url
) {
}
