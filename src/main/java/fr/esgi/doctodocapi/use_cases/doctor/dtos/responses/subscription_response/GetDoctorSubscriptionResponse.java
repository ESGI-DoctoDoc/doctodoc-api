package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.subscription_response;

import java.math.BigDecimal;
import java.util.UUID;

public record GetDoctorSubscriptionResponse(
        UUID id,
        DoctorSubscriptionInfo doctor,
        String start,
        String end,
        BigDecimal amount,
        String status,
        String createdAt
) {
}
