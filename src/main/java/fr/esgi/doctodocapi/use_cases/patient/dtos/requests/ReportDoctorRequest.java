package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReportDoctorRequest(
        @NotNull
        UUID doctorId,

        @NotBlank
        String explanation
) {
}
