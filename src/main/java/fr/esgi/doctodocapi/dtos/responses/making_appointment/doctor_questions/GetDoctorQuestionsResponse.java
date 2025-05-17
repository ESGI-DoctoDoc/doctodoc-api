package fr.esgi.doctodocapi.dtos.responses.making_appointment.doctor_questions;

public class GetDoctorQuestionsResponse {
    private final String type;
    private final String question;
    private final Boolean isMandatory;

    public GetDoctorQuestionsResponse(String type, String question, Boolean isMandatory) {
        this.type = type;
        this.question = question;
        this.isMandatory = isMandatory;
    }

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }
}
