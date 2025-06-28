package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response;

import java.util.UUID;

public record DoctorInfo(
        UUID id,
        boolean isOnboardingCompleted,
        String firstName,
        String lastName,
        boolean isLicenseActivated,
        boolean isVerified
) {
}
