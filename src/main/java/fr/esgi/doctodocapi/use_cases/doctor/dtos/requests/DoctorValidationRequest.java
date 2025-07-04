package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DoctorValidationRequest(
        @NotNull UUID doctorId
) {}