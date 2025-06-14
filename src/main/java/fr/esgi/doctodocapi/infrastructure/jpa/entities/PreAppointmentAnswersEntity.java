package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pre_appointment_answers")
public class PreAppointmentAnswersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_question_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentEntity appointment;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AppointmentEntity getAppointment() {
        return appointment;
    }

    public void setAppointment(AppointmentEntity appointment) {
        this.appointment = appointment;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreAppointmentAnswersEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}