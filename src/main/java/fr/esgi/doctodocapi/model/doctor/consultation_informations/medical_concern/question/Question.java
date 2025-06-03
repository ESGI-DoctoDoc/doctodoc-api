package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Question {
    private UUID id;
    private QuestionType type;
    private String questionName;
    private List<String> options;
    private boolean isMandatory;
    private UUID medicalConcernId;
    private LocalDate createdAt;

    public Question(UUID id, QuestionType type, String questionName, List<String> options, boolean isMandatory, UUID medicalConcernId, LocalDate createdAt) {
        this.id = id;
        this.type = type;
        this.questionName = questionName;
        this.options = options;
        this.isMandatory = isMandatory;
        this.medicalConcernId = medicalConcernId;
        this.createdAt = createdAt;
    }

    private Question(QuestionType type, String questionName, List<String> options, boolean isMandatory, UUID medicalConcernId, LocalDate createdAt) {
        this.type = type;
        this.questionName = questionName;
        this.options = options;
        this.isMandatory = isMandatory;
        this.medicalConcernId = medicalConcernId;
        this.createdAt = createdAt;
    }

    public static Question create(QuestionType type, String questionName, List<String> options, boolean isMandatory, UUID medicalConcernId) {
        return new Question(type, questionName, options, isMandatory, medicalConcernId, LocalDate.now());
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

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMedicalConcernId() {
        return medicalConcernId;
    }

    public void setMedicalConcernId(UUID medicalConcernId) {
        this.medicalConcernId = medicalConcernId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
