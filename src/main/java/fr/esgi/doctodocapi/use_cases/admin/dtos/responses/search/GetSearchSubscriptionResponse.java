package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.search;

import java.util.UUID;

public record GetSearchSubscriptionResponse(
        UUID id,
        Doctor doctor,
        String start,
        String end,
        double amount,
        String status
) {
    public record Doctor(
            UUID id,
            String firstName,
            String lastName,
            String email
    ) {}
}
