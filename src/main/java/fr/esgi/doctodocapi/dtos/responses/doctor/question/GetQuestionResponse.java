package fr.esgi.doctodocapi.dtos.responses.doctor.question;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetQuestionResponse(
        String question,
        String type,
        List<String> options,
        Boolean isMandatory,
        UUID medicalConcernId,
        LocalDate createdAt
) {
}
