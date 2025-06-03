package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

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
}
