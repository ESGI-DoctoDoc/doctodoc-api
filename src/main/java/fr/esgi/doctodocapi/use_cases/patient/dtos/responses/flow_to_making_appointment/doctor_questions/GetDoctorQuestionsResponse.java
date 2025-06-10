package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.doctor_questions;

public class GetDoctorQuestionsResponse {
    private final String id;
    private final String type;
    private final String question;
    private final Boolean isMandatory;

    public GetDoctorQuestionsResponse(String id, String type, String question, Boolean isMandatory) {
        this.id = id;
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

    public String getId() {
        return id;
    }
}
