package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Question persistence operations.
 */
public interface QuestionRepository {
    /**
     * Saves a question entity.
     *
     * @param question the Question to save
     * @return the saved Question instance
     */
    Question save(Question question);

    /**
     * Deletes a question entity.
     *
     * @param id the Question id to delete
     */
    void delete(UUID id);

    List<Question> getByMedicalConcernId(UUID medicalConcernId);
}
