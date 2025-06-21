package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_information_response;

public record DoctorInfoResponse(
        UserInfo user,
        boolean hasCompletedOnboarding,
        boolean hasLicense,
        boolean isVerified
) {
}
