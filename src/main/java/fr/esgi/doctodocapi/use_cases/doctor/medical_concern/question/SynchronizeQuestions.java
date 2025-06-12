package fr.esgi.doctodocapi.use_cases.doctor.medical_concern.question;

import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionInput;
import fr.esgi.doctodocapi.dtos.requests.doctor.medical_concern.question.QuestionsInputRequest;
import fr.esgi.doctodocapi.dtos.responses.doctor.medical_concern.question.GetQuestionResponse;
import fr.esgi.doctodocapi.exceptions.ApiException;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for synchronizing a medical concern's list of questions with a new list.
 * <p>
 * This includes:
 * - Deleting obsolete questions (not present in the incoming list),
 * - Saving or updating the new questions.
 */
@Service
public class SynchronizeQuestions {
    private final MedicalConcernRepository medicalConcernRepository;
    private final DeleteObsoleteQuestions deleteObsoleteQuestions;
    private final SaveQuestions saveQuestions;

    public SynchronizeQuestions(MedicalConcernRepository medicalConcernRepository, DeleteObsoleteQuestions deleteObsoleteQuestions, SaveQuestions saveQuestions) {
        this.medicalConcernRepository = medicalConcernRepository;
        this.deleteObsoleteQuestions = deleteObsoleteQuestions;
        this.saveQuestions = saveQuestions;
    }

    /**
     * Synchronizes the medical concern's questions by:
     * - Removing questions not in the new list,
     * - Creating or updating the ones in the incoming list.
     *
     * @param medicalConcernId the ID of the medical concern
     * @param request           the incoming question list
     * @return the list of synchronized {@link GetQuestionResponse}
     * @throws ApiException if the medical concern does not exist or a domain exception occurs
     */
    public List<GetQuestionResponse> execute(UUID medicalConcernId, QuestionsInputRequest request) {
        try {
            MedicalConcern medicalConcern = this.medicalConcernRepository.getById(medicalConcernId);

            List<QuestionInput> incoming = request.questions();
            List<UUID> incomingIds = incoming.stream()
                    .map(QuestionInput::id)
                    .filter(java.util.Objects::nonNull)
                    .toList();

            this.deleteObsoleteQuestions.execute(medicalConcernId, incomingIds);
            return this.saveQuestions.execute(medicalConcern.getId(), incoming);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}