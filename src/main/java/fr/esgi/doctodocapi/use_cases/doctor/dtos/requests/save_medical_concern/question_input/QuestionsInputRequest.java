package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionsInputRequest(
        @NotNull @Valid List<QuestionInput> questions
) {}
