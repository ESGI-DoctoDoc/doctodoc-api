package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account;

public record UpdateDoctorProfileRequest(
        String firstname,
        String lastname,
        String bio,
        String address,
        String profilePictureUrl
) {
}
