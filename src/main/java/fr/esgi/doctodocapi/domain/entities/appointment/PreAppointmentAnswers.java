package fr.esgi.doctodocapi.domain.entities.appointment;

import fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.question.Question;

public class PreAppointmentAnswers {
    private Question question;
    private String response;

    public PreAppointmentAnswers(Question question, String response) {
        this.question = question;
        this.response = response;
    }

    public static PreAppointmentAnswers of(Question question, String response) {
        return new PreAppointmentAnswers(question, response);
    }

    public Question getQuestion() {
        return question;
    }

    public String getResponse() {
        return response;
    }
}
