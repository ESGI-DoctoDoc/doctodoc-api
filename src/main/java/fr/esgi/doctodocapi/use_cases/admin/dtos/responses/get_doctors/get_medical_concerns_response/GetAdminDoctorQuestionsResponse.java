package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GetAdminDoctorQuestionsResponse(
        UUID id,
        String question,
        String type,
        List<Map<String, String>> options,
        boolean isMandatory,
        LocalDate createdAt
) {
}
