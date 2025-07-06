package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response;

import java.util.UUID;

public record DoctorSubscriptionInfo(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
