package fr.esgi.doctodocapi.use_cases.admin.dtos.responses.get_doctors.get_medical_concerns_response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetAdminDoctorMedicalConcernsResponse(
        UUID id,
        String name,
        Integer duration,
        Double price,
        List<GetAdminDoctorQuestionsResponse> questions,
        LocalDate createdAt
) {
}
