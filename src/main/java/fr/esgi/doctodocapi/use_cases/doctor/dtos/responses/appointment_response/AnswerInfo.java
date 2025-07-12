package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response;

import java.util.UUID;

public record AnswerInfo(
        UUID id,
        String question,
        String answer
) {
}
