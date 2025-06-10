package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for persistence operations related to {@link MedicalConcern}.
 * <p>
 * Provides methods for retrieving, saving, and managing medical concerns and their associated questions.
 */
public interface MedicalConcernRepository {

    /**
     * Retrieves all medical concerns created by a specific doctor.
     *
     * @param doctor the doctor whose medical concerns are to be fetched
     * @return a list of {@link MedicalConcern} associated with the given doctor
     */
    List<MedicalConcern> getMedicalConcerns(Doctor doctor);

    /**
     * Retrieves all questions associated with a given medical concern.
     *
     * @param medicalConcern the medical concern whose questions should be retrieved
     * @return a list of {@link Question} associated with the medical concern
     */
    List<Question> getDoctorQuestions(MedicalConcern medicalConcern);

    /**
     * Retrieves a specific question by its unique identifier.
     *
     * @param uuid the unique identifier of the question
     * @return the corresponding {@link Question}
     * @throws QuestionNotFoundException if no question with the specified ID exists
     */
    Question getQuestionById(UUID uuid) throws QuestionNotFoundException;

    /**
     * Retrieves a medical concern by its unique identifier.
     *
     * @param id the unique identifier of the medical concern
     * @return the corresponding {@link MedicalConcern}
     * @throws MedicalConcernNotFoundException if no medical concern with the specified ID exists
     */
    MedicalConcern getById(UUID id) throws MedicalConcernNotFoundException;

    /**
     * Saves or updates a {@link MedicalConcern}.
     *
     * @param medicalConcern the medical concern to be persisted
     * @return the saved or updated {@link MedicalConcern}
     */
    MedicalConcern save(MedicalConcern medicalConcern);
}
