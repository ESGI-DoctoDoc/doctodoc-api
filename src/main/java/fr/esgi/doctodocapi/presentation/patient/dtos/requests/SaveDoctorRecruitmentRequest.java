package fr.esgi.doctodocapi.presentation.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record SaveDoctorRecruitmentRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName
) {
}
