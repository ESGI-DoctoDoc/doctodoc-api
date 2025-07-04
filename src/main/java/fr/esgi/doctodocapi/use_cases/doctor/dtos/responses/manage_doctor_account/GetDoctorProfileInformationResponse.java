package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account;

import java.util.UUID;

public record GetDoctorProfileInformationResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        AddressProfileInfo address,
        SpecialityProfileInfo speciality,
        SubscriptionProfileInfo subscription,
        String bio,
        String profilePictureUrl
) {
}
