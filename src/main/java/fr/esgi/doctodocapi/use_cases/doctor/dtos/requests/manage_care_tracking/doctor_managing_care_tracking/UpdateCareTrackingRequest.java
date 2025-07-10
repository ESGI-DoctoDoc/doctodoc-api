package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.doctor_managing_care_tracking;

import jakarta.validation.constraints.NotBlank;

public record UpdateCareTrackingRequest(
        @NotBlank String name,
        String description
) {
    public UpdateCareTrackingRequest {
        name = name.trim();
        if(!description.isEmpty()) {
            description = description.trim();
        }
    }
}
