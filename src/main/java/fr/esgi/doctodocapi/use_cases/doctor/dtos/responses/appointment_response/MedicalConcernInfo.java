package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.UUID;

public record MedicalConcernInfo(
        UUID id,
        String name
) { }
