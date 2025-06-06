package fr.esgi.doctodocapi.dtos.requests.admin;

import jakarta.validation.constraints.NotBlank;

public record AddSpecialityRequest(
        @NotBlank String name
) {
    public AddSpecialityRequest {
        name = name.trim();
    }
}
