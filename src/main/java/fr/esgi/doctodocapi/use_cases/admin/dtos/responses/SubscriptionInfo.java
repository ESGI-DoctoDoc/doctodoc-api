package fr.esgi.doctodocapi.use_cases.admin.dtos.responses;

import java.util.UUID;

public record SubscriptionInfo(
        UUID id,
        String start,
        String end
) {
}
