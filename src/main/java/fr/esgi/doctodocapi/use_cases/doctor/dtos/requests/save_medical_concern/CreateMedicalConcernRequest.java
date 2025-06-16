package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateMedicalConcernRequest(
        @NotBlank String name,
        @NotNull @Min(1) Integer duration,
        @NotNull @DecimalMin("0.0") Double price,
        @Valid List<QuestionInput> questions
) {
    public CreateMedicalConcernRequest {
        name = name.trim();
    }
}
