package fr.esgi.doctodocapi.use_cases.admin.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record AddSpecialityRequest(
        @NotBlank String name
) {
    public AddSpecialityRequest {
        name = name.trim();
    }
}
