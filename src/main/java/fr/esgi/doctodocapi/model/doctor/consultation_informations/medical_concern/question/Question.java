package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

import java.util.List;
import java.util.UUID;

public class Question {
    private UUID id;
    private QuestionType type;
    private String questionName;
    private List<String> options;
    private boolean isMandatory;

    public Question(UUID id, QuestionType type, String questionName, List<String> options, boolean isMandatory) {
        this.id = id;
        this.type = type;
        this.questionName = questionName;
        this.options = options;
        this.isMandatory = isMandatory;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getQuestion() {
        return questionName;
    }

    public void setQuestion(String questionName) {
        this.questionName = questionName;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public UUID getId() {
        return id;
    }
}
