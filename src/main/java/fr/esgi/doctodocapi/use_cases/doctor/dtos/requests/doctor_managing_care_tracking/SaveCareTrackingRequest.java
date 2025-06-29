package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.doctor_managing_care_tracking;

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
