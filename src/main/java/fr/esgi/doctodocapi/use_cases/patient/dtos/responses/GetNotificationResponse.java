package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetNotificationResponse(
        UUID id,
        String title,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime sendAt,
        boolean isRead
) {
}
