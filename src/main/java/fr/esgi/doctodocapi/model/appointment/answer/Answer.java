package fr.esgi.doctodocapi.model.appointment.answer;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;

public class Answer {
    private Question question;
    private String response;

    public Answer(Question question, String response) {
        this.question = question;
        this.response = response;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
