package fr.esgi.doctodocapi.dtos.requests.doctor;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DoctorValidationRequest(
        @NotNull UUID doctorId
) {}