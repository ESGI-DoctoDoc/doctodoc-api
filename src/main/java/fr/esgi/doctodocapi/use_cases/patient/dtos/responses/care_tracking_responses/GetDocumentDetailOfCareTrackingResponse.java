package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentUser;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetDocumentDetailOfCareTrackingResponse(
        UUID id,
        String type,
        String name,
        String url,
        boolean isShared,
        LocalDateTime uploadedAt,
        GetDocumentUser uploadedByUser
) {
}
