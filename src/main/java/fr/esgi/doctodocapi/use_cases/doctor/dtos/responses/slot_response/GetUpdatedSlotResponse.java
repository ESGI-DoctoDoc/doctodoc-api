package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response;

import java.util.List;
import java.util.UUID;

public record GetUpdatedSlotResponse(
        UUID id,
        String startHour,
        String endHour,
        List<MedicalConcernUpdateResponse> medicalConcerns
) {
    public record MedicalConcernUpdateResponse(
            UUID id,
            String name
    ) {
    }
}
