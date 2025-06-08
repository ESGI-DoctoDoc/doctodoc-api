package fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionsInputRequest(
        @NotNull @Valid List<QuestionInput> questions
) {}
