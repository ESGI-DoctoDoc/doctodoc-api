package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record SaveDocumentRequest(
        @NotBlank String filename,
        @NotBlank String type
) {
    public SaveDocumentRequest {
        filename = filename.trim();
        type = type.trim();
    }
}
