package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import java.util.UUID;

public record GetFileResponse(
        UUID id,
        String name,
        String url
) {
}
