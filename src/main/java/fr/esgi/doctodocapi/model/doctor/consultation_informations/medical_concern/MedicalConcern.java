package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MedicalConcern {
    private UUID id;
    private String name;
    private Duration durationInMinutes;
    private Double price;
    private List<Question> questions;

    public MedicalConcern(UUID id, String name, Integer durationInMinutesValue, List<Question> questions, Double price) {
        this.id = id;
        this.name = name;
        this.durationInMinutes = Duration.of(durationInMinutesValue);
        this.questions = questions;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Duration durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MedicalConcern that = (MedicalConcern) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
