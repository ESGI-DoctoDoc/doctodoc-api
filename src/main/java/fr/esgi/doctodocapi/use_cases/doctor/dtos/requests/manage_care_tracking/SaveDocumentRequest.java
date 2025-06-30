package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking;

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
