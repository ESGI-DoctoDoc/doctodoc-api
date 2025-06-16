package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GetMedicalConcernResponse(
        UUID id,
        String name,
        Integer duration,
        Double price,
        UUID doctorId,
        List<GetQuestionResponse> questions,
        LocalDate createdAt
) {}
