package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document;

import java.util.UUID;

public record GetDocumentResponse(
        UUID id,
        String name,
        String type,
        String url
) {
}
