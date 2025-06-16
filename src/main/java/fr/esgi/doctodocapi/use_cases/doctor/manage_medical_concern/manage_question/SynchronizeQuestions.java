package fr.esgi.doctodocapi.use_cases.doctor.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionInput;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_medical_concern.question_input.QuestionsInputRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.medical_concern_response.question_response.GetQuestionResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.IDeleteObsoleteQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.ISaveQuestions;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_medical_concern.manage_question.ISynchronizeQuestions;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

/**
 * Use case for synchronizing a medical concern's list of questions with a new list.
 * <p>
 * This includes:
 * - Deleting obsolete questions (not present in the incoming list),
 * - Saving or updating the new questions.
 */
public class SynchronizeQuestions implements ISynchronizeQuestions {
    private final MedicalConcernRepository medicalConcernRepository;
    private final IDeleteObsoleteQuestions deleteObsoleteQuestions;
    private final ISaveQuestions saveQuestions;

    public SynchronizeQuestions(MedicalConcernRepository medicalConcernRepository, IDeleteObsoleteQuestions deleteObsoleteQuestions, ISaveQuestions saveQuestions) {
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
     * @param request          the incoming question list
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