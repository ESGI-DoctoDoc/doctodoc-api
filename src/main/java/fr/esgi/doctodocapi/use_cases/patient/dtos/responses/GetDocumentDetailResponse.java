package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetDocumentDetailResponse(
        UUID id,
        String type,
        String name,
        String url,
        LocalDateTime uploadedAt
//        LocalDateTime uploadedBy,
) {
}
