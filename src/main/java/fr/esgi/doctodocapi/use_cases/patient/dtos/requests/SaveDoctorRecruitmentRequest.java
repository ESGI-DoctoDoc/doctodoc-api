package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record SaveDoctorRecruitmentRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName
) {
}
