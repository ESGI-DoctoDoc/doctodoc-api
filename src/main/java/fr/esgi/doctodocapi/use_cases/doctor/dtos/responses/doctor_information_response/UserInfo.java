package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response;

import java.util.UUID;

public record UserInfo(
        UUID id,
        String email,
        String phoneNumber,
        String role,
        boolean isEmailVerified,
        boolean isDoubleAuthActive
) {
}
