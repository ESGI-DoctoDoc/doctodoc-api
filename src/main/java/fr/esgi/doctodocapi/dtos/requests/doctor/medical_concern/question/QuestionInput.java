package fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question;

import jakarta.validation.constraints.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record QuestionInput(
        UUID id,
        @NotBlank String type,
        @NotBlank String question,
        List<OptionInput> options,
        @NotNull Boolean isMandatory
) {
    public QuestionInput {
        type = type.trim();
        question = question.trim();
        if (options == null) {
            options = Collections.emptyList();
        } else {
            options = options.stream()
                    .filter(option -> option != null && option.label() != null && !option.label().isBlank())
                    .toList();
        }
    }

    public List<String> extractOptionLabels() {
        return options.stream().map(OptionInput::label).toList();
    }
}
