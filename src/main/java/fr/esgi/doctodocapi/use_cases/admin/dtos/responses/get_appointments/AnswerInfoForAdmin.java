package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_appointments;

import java.util.UUID;

public record AnswerInfoForAdmin(
        UUID id,
        String question,
        String answer
) {
}
