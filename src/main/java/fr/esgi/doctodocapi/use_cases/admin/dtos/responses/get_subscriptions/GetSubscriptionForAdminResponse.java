package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_subscriptions;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.DoctorInfoForAdmin;

import java.math.BigDecimal;
import java.util.UUID;

public record GetSubscriptionForAdminResponse(
        UUID id,
        DoctorInfoForAdmin doctor,
        String start,
        String end,
        BigDecimal amount,
        String status,
        String createdAt
) {
}
