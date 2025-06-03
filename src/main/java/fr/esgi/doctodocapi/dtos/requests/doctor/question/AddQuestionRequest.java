package fr.esgi.doctodocapi.dtos.requests.doctor.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddQuestionRequest(
        @NotBlank String type,
        @NotBlank String question,
        @NotEmpty List<String> options,
        @NotNull Boolean isMandatory
) {
        public AddQuestionRequest {
                question = question.trim();
                options = options.stream()
                        .map(String::trim)
                        .filter(opt -> !opt.isBlank())
                        .toList();
                type = type.trim();
        }
}