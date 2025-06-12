package fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern;

import fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern.question.GetQuestionResponse;

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
