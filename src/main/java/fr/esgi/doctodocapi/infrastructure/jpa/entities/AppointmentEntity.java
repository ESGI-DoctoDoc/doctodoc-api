package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@SQLRestriction("deleted_at IS NULL")
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "appointment_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private SlotEntity slot;

    @ManyToOne
    @JoinColumn(name = "medical_concern_id", nullable = false)
    private MedicalConcernEntity medicalConcern;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_hour", nullable = false)
    private LocalTime startHour;

    @Column(name = "end_hour", nullable = false)
    private LocalTime endHour;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt = LocalDateTime.now();

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "appointment")
    private List<PreAppointmentAnswersEntity> appointmentQuestions;

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

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public SlotEntity getSlot() {
        return slot;
    }

    public void setSlot(SlotEntity slot) {
        this.slot = slot;
    }

    public MedicalConcernEntity getMedicalConcern() {
        return medicalConcern;
    }

    public void setMedicalConcern(MedicalConcernEntity medicalConcern) {
        this.medicalConcern = medicalConcern;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public LocalDateTime getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(LocalDateTime lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PreAppointmentAnswersEntity> getAppointmentQuestions() {
        return appointmentQuestions;
    }

    public void setAppointmentQuestions(List<PreAppointmentAnswersEntity> appointmentQuestions) {
        this.appointmentQuestions = appointmentQuestions;
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
        if (!(o instanceof AppointmentEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
