package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.QuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.QuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.QuestionMapper;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionNotFoundException;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the QuestionRepository interface.
 * This service handles operations related to question entities,
 * including saving domain questions to the database.
 */
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    private final QuestionMapper questionMapper;
    private final QuestionJpaRepository questionJpaRepository;

    public QuestionRepositoryImpl(QuestionMapper questionMapper, QuestionJpaRepository questionJpaRepository) {
        this.questionMapper = questionMapper;
        this.questionJpaRepository = questionJpaRepository;
    }

    /**
     * Saves a question domain object to the database.
     * This method maps the domain model to a JPA entity before saving,
     * then maps the persisted entity back to the domain model.
     *
     * @param question The question domain object to save
     * @return The saved question as a domain object
     */
    @Override
    public Question save(Question question) {
        QuestionEntity entityToBeSaved = this.questionMapper.toEntity(question);
        this.questionJpaRepository.save(entityToBeSaved);
        return this.questionMapper.toDomain(entityToBeSaved);
    }

    /**
     * Soft-deletes a question by setting its {@code deletedAt} timestamp.
     *
     * @param id the UUID of the question to delete
     * @throws QuestionNotFoundException if the question is not found
     */
    @Override
    public void delete(UUID id) {
        QuestionEntity entityToDelete = this.questionJpaRepository.findById(id).orElseThrow(QuestionNotFoundException::new);
        entityToDelete.setDeletedAt(LocalDateTime.now());
        this.questionJpaRepository.save(entityToDelete);
    }

    /**
     * Retrieves all questions associated with the given medical concern that are not soft-deleted.
     *
     * @param medicalConcernId the ID of the associated medical concern
     * @return a list of {@link Question} domain objects
     */
    @Override
    public List<Question> getByMedicalConcernId(UUID medicalConcernId) {
        return this.questionJpaRepository
                .findAllByMedicalConcern_IdAndDeletedAtIsNull(medicalConcernId)
                .stream()
                .map(this.questionMapper::toDomain)
                .toList();
    }
}
