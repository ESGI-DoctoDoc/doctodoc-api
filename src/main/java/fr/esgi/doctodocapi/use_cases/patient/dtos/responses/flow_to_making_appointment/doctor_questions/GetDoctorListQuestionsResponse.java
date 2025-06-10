package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.doctor_questions;

import java.util.List;

public class GetDoctorListQuestionsResponse extends GetDoctorQuestionsResponse {
    private final List<String> options;

    public GetDoctorListQuestionsResponse(String id, String type, String question, Boolean isMandatory, List<String> options) {
        super(id, type, question, isMandatory);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }
}
