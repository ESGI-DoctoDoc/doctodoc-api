package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateMedicalConcernRequest(
        @NotBlank String name,
        @NotNull @Min(1) Integer durationInMinutes,
        @NotNull @Min(0) Double price
) {
    public UpdateMedicalConcernRequest {
        name = name.trim();
    }
}
