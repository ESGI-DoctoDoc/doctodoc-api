package fr.esgi.doctodocapi.domain.use_cases.doctor.manage_medical_concern.manage_question;

import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case responsible for deleting obsolete questions associated with a medical concern.
 * <p>
 * Questions that are not included in the incoming list of question IDs are considered obsolete and deleted.
 */
@Service
public class DeleteObsoleteQuestions {
    private final QuestionRepository questionRepository;

    public DeleteObsoleteQuestions(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Deletes all questions linked to the specified medical concern that are not in the list of incoming IDs.
     *
     * @param medicalConcernId the ID of the associated medical concern
     * @param incomingIds      the list of question IDs that should be retained
     */
    public void execute(UUID medicalConcernId, List<UUID> incomingIds) {
        List<Question> existing = this.questionRepository.getByMedicalConcernId(medicalConcernId);
        for (Question question : existing) {
            if (!incomingIds.contains(question.getId())) {
                this.questionRepository.delete(question.getId());
            }
        }
    }
}
