package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response;

import java.time.LocalDateTime;

public record UserInfo(
        String email,
        String phoneNumber,
        boolean isEmailVerified,
        boolean isDoubleAuthActive,
        String doubleAuthCode,
        LocalDateTime createdAt
) {
}
