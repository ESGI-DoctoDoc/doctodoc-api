package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "care_trackings")
public class CareTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "care_tracking_id")
    private UUID id;

    @Column(name = "case_name", nullable = false)
    private String caseName;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity creator;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private PatientEntity patient;

    @Column(name = "doctors", columnDefinition = "uuid[]", nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<UUID> doctors;

    @OneToMany(mappedBy = "care_tracking")
    private List<AppointmentEntity> appointments;

    @OneToMany(mappedBy = "care_tracking")
    private List<CareTrackingTraceEntity> careTrackingTraces;

    @Column(name = "documents", columnDefinition = "text[]", nullable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> documents;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "closed_at", nullable = false)
    private LocalDate closedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DoctorEntity getCreator() {
        return creator;
    }

    public void setCreator(DoctorEntity creator) {
        this.creator = creator;
    }

    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    public List<UUID> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<UUID> doctors) {
        this.doctors = doctors;
    }

    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    public List<CareTrackingTraceEntity> getCareTrackingTraces() {
        return careTrackingTraces;
    }

    public void setCareTrackingTraces(List<CareTrackingTraceEntity> careTrackingTraces) {
        this.careTrackingTraces = careTrackingTraces;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDate closedAt) {
        this.closedAt = closedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CareTrackingEntity that = (CareTrackingEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
