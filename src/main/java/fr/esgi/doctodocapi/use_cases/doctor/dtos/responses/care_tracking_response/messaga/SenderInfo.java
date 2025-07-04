package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga;

import java.util.UUID;

public record SenderInfo(
        UUID id,
        String name,
        String avatarUrl
) {
}
