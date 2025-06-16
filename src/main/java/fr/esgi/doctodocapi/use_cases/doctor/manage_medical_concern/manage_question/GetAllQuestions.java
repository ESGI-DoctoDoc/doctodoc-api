package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IGetAllQuestions;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

/**
 * Use case for retrieving all questions linked to a specific medical concern.
 * This service fetches the medical concern by its identifier and retrieves
 * all associated questions, which are then transformed into response DTOs.
 */
public class GetAllQuestions implements IGetAllQuestions {
    private final MedicalConcernRepository repository;

    public GetAllQuestions(MedicalConcernRepository repository) {
        this.repository = repository;
    }

    /**
     * Executes the retrieval of questions associated with a medical concern.
     * If the medical concern does not exist or a domain rule is violated,
     * a {@link ApiException} is thrown with a corresponding error message.
     *
     * @param medicalConcernId the identifier of the medical concern
     * @return a list of {@link GetQuestionResponse} representing all related questions
     * @throws ApiException if the medical concern is not found or a domain exception occurs
     */
    public List<GetQuestionResponse> execute(UUID medicalConcernId) {
        try {
            MedicalConcern concern = this.repository.getById(medicalConcernId);
            List<Question> questions = this.repository.getDoctorQuestions(concern);

            return questions.stream()
                    .map(GetQuestionResponse::fromDomain)
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
