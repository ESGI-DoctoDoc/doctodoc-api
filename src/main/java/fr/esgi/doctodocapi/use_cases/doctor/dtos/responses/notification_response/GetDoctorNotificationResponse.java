package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetDoctorNotificationResponse(
        UUID id,
        String title,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime createdAt,
        boolean isRead
) {
}
