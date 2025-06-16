package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import java.time.LocalDate;
import java.util.UUID;

public record GetSpecialityResponse(
        UUID id,
        String name,
        LocalDate createdAt
) {
}
