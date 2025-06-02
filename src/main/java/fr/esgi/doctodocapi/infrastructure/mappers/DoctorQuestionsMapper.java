package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
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
                entity.isMandatory()
        );
    }
}
