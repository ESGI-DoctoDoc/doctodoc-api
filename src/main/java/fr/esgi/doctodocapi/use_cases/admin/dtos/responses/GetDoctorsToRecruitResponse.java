package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetDoctorsToRecruitResponse(
        UUID id,
        String firstName,
        String lastName,
        LocalDateTime createdAt
) {
}
