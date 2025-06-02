package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "medical_concerns")
public class MedicalConcernEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "medical_concern_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @OneToMany(mappedBy = "medicalConcern")
    private List<DoctorQuestionEntity> questions;

    @ManyToMany(mappedBy = "medicalConcerns")
    private List<SlotEntity> slots;

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

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<DoctorQuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DoctorQuestionEntity> questions) {
        this.questions = questions;
    }

    public List<SlotEntity> getSlots() {
        return slots;
    }

    public void setSlots(List<SlotEntity> slots) {
        this.slots = slots;
    }
}
