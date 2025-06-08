package fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern;

import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionInput;
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
