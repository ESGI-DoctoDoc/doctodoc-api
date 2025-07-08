package fr.esgi.doctodocapi.use_cases.patient.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SaveReferentDoctorRequest(
        @NotNull
        UUID doctorId
) {
}
