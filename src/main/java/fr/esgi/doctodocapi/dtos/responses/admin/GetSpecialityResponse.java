package fr.esgi.doctodocapi.dtos.responses.admin;

import java.time.LocalDate;
import java.util.UUID;

public record GetSpecialityResponse(
        UUID id,
        String name,
        LocalDate createdAt
) {
}
