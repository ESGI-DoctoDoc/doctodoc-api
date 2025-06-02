package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.PreAppointmentAnswersEntity;
import fr.esgi.doctodocapi.model.appointment.PreAppointmentAnswers;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import org.springframework.stereotype.Service;

@Service
public class PreAppointmentAnswersMapper {
    public PreAppointmentAnswers toDomain(PreAppointmentAnswersEntity entity, Question question) {
        return new PreAppointmentAnswers(
                question,
                entity.getAnswer()
        );
    }

    public PreAppointmentAnswersEntity toEntity(String answer, AppointmentEntity appointmentEntity, DoctorQuestionEntity doctorQuestionEntity) {
        PreAppointmentAnswersEntity entity = new PreAppointmentAnswersEntity();
        entity.setAppointment(appointmentEntity);
        entity.setQuestion(doctorQuestionEntity);
        entity.setAnswer(answer);

        return entity;
    }
}
