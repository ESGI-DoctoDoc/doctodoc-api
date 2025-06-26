package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record SubscribeRequest(
        @NotBlank String successUrl,
        @NotBlank String cancelUrl
) {
}
