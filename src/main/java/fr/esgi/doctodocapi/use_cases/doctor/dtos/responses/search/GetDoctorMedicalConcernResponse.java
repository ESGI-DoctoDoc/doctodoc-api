package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.search;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;

import java.util.List;
import java.util.UUID;

public record GetDoctorMedicalConcernResponse(
        UUID id,
        String name,
        int duration,
        double price,
        List<GetQuestionResponse> questions
) {
}