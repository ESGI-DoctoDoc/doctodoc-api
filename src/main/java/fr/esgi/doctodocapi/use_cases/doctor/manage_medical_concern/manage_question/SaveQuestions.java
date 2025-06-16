package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionInput;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case responsible for saving or updating a list of questions linked to a medical concern.
 * <p>
 * If a question contains an ID, it is treated as an update; otherwise, it is considered a new creation.
 * Each question is persisted and returned as a {@link GetQuestionResponse}.
 */
@Service
public class SaveQuestions {
    private final QuestionRepository questionRepository;

    public SaveQuestions(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Saves the given list of questions, creating or updating each one as needed.
     *
     * @param medicalConcernId the ID of the associated medical concern
     * @param inputs           the list of question inputs to process
     * @return a list of {@link GetQuestionResponse} representing the persisted questions
     */
    public List<GetQuestionResponse> execute(UUID medicalConcernId, List<QuestionInput> inputs) {
        return inputs.stream()
                .map(input -> {
                    Question question = Question.create(
                            QuestionType.valueOf(input.type().toUpperCase()),
                            input.question(),
                            input.extractOptionLabels(),
                            input.isMandatory(),
                            medicalConcernId
                    );
                    if (input.id() != null) {
                        question.setId(input.id());
                    }
                    return GetQuestionResponse.fromDomain(questionRepository.save(question));
                })
                .toList();
    }
}
