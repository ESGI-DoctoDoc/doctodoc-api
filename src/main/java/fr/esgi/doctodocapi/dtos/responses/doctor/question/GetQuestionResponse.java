package fr.esgi.doctodocapi.dtos.responses.doctor.question;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GetQuestionResponse(
        UUID id,
        String question,
        String type,
        List<Map<String, String>> options,
        Boolean isMandatory,
        LocalDate createdAt
) {
    public static GetQuestionResponse fromDomain(Question question) {
        List<Map<String, String>> mappedOptions = question.getOptions() == null ? List.of() :
                question.getOptions().stream()
                        .map(option -> Map.of(
                                "label", option,
                                "value", option.toLowerCase()
                        ))
                        .toList();

        return new GetQuestionResponse(
                question.getId(),
                question.getQuestion(),
                question.getType().getValue(),
                mappedOptions,
                question.isMandatory(),
                question.getCreatedAt()
        );
    }
}