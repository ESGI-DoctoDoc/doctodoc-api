package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorQuestionJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorQuestionsMapper;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionRepositoryImpl implements QuestionRepository {
    private final DoctorQuestionsMapper doctorQuestionsMapper;
    private final DoctorQuestionJpaRepository doctorQuestionJpaRepository;

    public QuestionRepositoryImpl(DoctorQuestionsMapper doctorQuestionsMapper, DoctorQuestionJpaRepository doctorQuestionJpaRepository) {
        this.doctorQuestionsMapper = doctorQuestionsMapper;
        this.doctorQuestionJpaRepository = doctorQuestionJpaRepository;
    }

    @Override
    public Question save(Question question) {
        DoctorQuestionEntity entityToBeSaved = this.doctorQuestionsMapper.toEntity(question);
        this.doctorQuestionJpaRepository.save(entityToBeSaved);
        return this.doctorQuestionsMapper.toDomain(entityToBeSaved);
    }
}
