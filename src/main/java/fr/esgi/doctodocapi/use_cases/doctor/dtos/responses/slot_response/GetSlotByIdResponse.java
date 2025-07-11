package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response;

import java.util.List;
import java.util.UUID;

public record GetSlotByIdResponse(
        UUID id,
        String day,
        String startHour,
        String endHour,
        String recurrence,
        Integer dayNumber,
        String start,
        String end,
        List<MedicalConcernResponse> medicalConcerns
) {
    public record MedicalConcernResponse(
            UUID id,
            String name,
            Integer duration
    ) {
    }
}