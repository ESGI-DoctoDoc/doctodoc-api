package fr.esgi.doctodocapi.use_cases.doctor.medical_concern.question;

import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionInput;
import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionsInputRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.question.GetQuestionResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for adding questions to a specific medical concern.
 * This service receives a list of questions and persists them using the domain model,
 * while validating the associated medical concern.
 */
@Service
public class AddQuestion {
    private final QuestionRepository questionRepository;
    private final MedicalConcernRepository medicalConcernRepository;

    public AddQuestion(QuestionRepository questionRepository, MedicalConcernRepository medicalConcernRepository) {
        this.questionRepository = questionRepository;
        this.medicalConcernRepository = medicalConcernRepository;
    }

    /**
     * Executes the addition of a list of questions linked to a specific medical concern.
     * Each question is mapped from the request DTO, validated, persisted, and returned as a response DTO.
     *
     * @param medicalConcernId        the identifier of the target medical concern
     * @param questionsInputRequest   the input containing the list of questions to create
     * @return a list of {@link GetQuestionResponse} representing the saved questions
     * @throws ApiException if the medical concern is not found or a domain validation fails
     */
    public List<GetQuestionResponse> execute(UUID medicalConcernId, QuestionsInputRequest questionsInputRequest) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);

            List<QuestionInput> questionInputs = questionsInputRequest.questions();
            if (questionInputs.isEmpty()) {
                return List.of();
            }

            return questionInputs.stream()
                    .map(questionInput -> {
                        Question question = Question.create(
                                QuestionType.valueOf(questionInput.type().toUpperCase()),
                                questionInput.question(),
                                questionInput.extractOptionLabels(),
                                questionInput.isMandatory(),
                                medicalConcern.getId()
                        );
                        Question saved = this.questionRepository.save(question);
                        return GetQuestionResponse.fromDomain(saved);
                    })
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
