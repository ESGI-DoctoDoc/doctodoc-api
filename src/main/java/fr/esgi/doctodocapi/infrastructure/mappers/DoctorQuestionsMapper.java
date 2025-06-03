package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import org.springframework.stereotype.Service;

@Service
public class DoctorQuestionsMapper {
    public Question toDomain(DoctorQuestionEntity entity) {
        return new Question(
                entity.getId(),
                QuestionType.fromValue(entity.getType()),
                entity.getQuestion(),
                entity.getOptions(),
                entity.isMandatory(),
                entity.getMedicalConcern().getId(),
                entity.getCreatedAt()
        );
    }

    public DoctorQuestionEntity toEntity(Question question) {
        DoctorQuestionEntity entity = new DoctorQuestionEntity();
        MedicalConcernEntity medicalConcernEntity = new MedicalConcernEntity();
        medicalConcernEntity.setId(question.getMedicalConcernId());
        entity.setMedicalConcern(medicalConcernEntity);
        entity.setId(question.getId());
        entity.setQuestion(question.getQuestion());
        entity.setType(question.getType().getValue());
        entity.setOptions(question.getOptions());
        entity.setMandatory(question.isMandatory());
        entity.setCreatedAt(question.getCreatedAt());
        return entity;
    }
}
