package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response;

import java.time.LocalDate;
import java.util.UUID;

public record GetUpdatedMedicalConcernResponse(
        UUID id,
        String name,
        Integer durationInMinutes,
        Double price,
        LocalDate createdAt
) {
}
