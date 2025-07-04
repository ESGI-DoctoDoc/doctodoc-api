package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "slots")
@SQLRestriction("deleted_at IS NULL")
public class SlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "slot_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @Column(name = "recurrence_id")
    private UUID recurrenceId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_hour", nullable = false)
    private LocalTime startHour;

    @Column(name = "end_hour", nullable = false)
    private LocalTime endHour;

    @ManyToMany
    @JoinTable(
            name = "slots_medical_concerns",
            joinColumns = @JoinColumn(name = "slot_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_concern_id")
    )
    private List<MedicalConcernEntity> medicalConcerns;

    @OneToMany(mappedBy = "slot")
    private List<AppointmentEntity> appointments;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "deleted_at", nullable = false)
    private LocalDate deletedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public List<MedicalConcernEntity> getMedicalConcerns() {
        return medicalConcerns;
    }

    public void setMedicalConcerns(List<MedicalConcernEntity> medicalConcerns) {
        this.medicalConcerns = medicalConcerns;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UUID getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(UUID recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}

