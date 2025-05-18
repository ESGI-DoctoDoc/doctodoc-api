package fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.doctor_questions;

import java.util.List;

public class GetDoctorListQuestionsResponse extends GetDoctorQuestionsResponse {
    private final List<String> options;

    public GetDoctorListQuestionsResponse(String type, String question, Boolean isMandatory, List<String> options) {
        super(type, question, isMandatory);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }
}
