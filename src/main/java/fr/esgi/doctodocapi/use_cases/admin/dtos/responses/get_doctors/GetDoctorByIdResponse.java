package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.CounterInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SpecialityInfo;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.SubscriptionInfo;

import java.util.List;
import java.util.UUID;

public record GetDoctorByIdResponse(
        UUID id,
        String firstName,
        String lastName,
        String bio,
        String email,
        String phone,
        String birthdate,
        String rpps,
        List<String> files,
        boolean isVerified,
        boolean isEmailVerified,
        String createdAt,
        SpecialityInfo speciality,
        List<SubscriptionInfo> subscriptions,
        CounterInfo counter,
        boolean isReported,
        AddressInfo address
) {
}
