package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a medical concern defined by a doctor.
 * <p>
 * A medical concern includes a name, duration, price, list of associated questions,
 * the doctor who created it, and its creation date.
 */
public class MedicalConcern {
    private UUID id;
    private String name;
    private Duration durationInMinutes;
    private Double price;
    private UUID doctorId;
    private List<Question> questions;
    private LocalDate createdAt;

    public MedicalConcern(UUID id, String name, Integer durationInMinutesValue, List<Question> questions, Double price, UUID doctorId, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.durationInMinutes = Duration.of(durationInMinutesValue);
        this.questions = questions;
        this.price = price;
        this.doctorId = doctorId;
        this.createdAt = createdAt;
    }

    private MedicalConcern(String name, Integer durationInMinutesValue, List<Question> questions, Double price, UUID doctorId, LocalDate createdAt) {
        this.name = name;
        this.durationInMinutes = Duration.of(durationInMinutesValue);
        this.questions = questions;
        this.price = price;
        this.doctorId = doctorId;
        this.createdAt = createdAt;
    }

    /**
     * Static factory method to create a new {@code MedicalConcern}.
     * The {@code createdAt} date is automatically set to today.
     *
     * @param name                   concern name
     * @param durationInMinutesValue duration in minutes
     * @param questions              list of associated questions
     * @param price                  concern price
     * @param doctorId               identifier of the doctor creating the concern
     * @return a new {@code MedicalConcern} instance
     */
    public static MedicalConcern create(String name, Integer durationInMinutesValue, List<Question> questions, Double price, UUID doctorId) {
        return new MedicalConcern(name, durationInMinutesValue, questions, price, doctorId, LocalDate.now());
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

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
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
