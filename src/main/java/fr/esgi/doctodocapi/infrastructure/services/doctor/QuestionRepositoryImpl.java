package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.QuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.QuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.QuestionMapper;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of the QuestionRepository interface.
 * This service handles operations related to question entities,
 * including saving domain questions to the database.
 */
@Service
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
}
