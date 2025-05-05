package fr.esgi.doctodocapi.dtos.requests.patient;

import jakarta.validation.constraints.NotBlank;

public record SaveDoctorRecruitmentRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName
) {
}
