package fr.esgi.doctodocapi.use_cases.user.dtos.requests;

import java.util.UUID;

public record ValidateEmailRequest(UUID userId) {
}
