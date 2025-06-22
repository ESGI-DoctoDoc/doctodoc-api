package fr.esgi.doctodocapi.use_cases.care_tracking.dtos.requests;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record SaveCareTrackingRequest(
        @NotNull UUID patientId,
        @NotBlank String name,
        String description
) {
    public SaveCareTrackingRequest {
        name = name.trim();
        if(description != null) {
            description = description.trim();
        }
    }
}
