package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "care_tracking_traces")
@SQLRestriction("deleted_at IS NULL")
public class CareTrackingTraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "care_tracking_trace_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "care_tracking_id", nullable = false)
    private CareTrackingEntity careTracking;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorEntity consultedBy;

    @Column(name = "consulted_at")
    private LocalDateTime consultedAt;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CareTrackingEntity getCareTracking() {
        return careTracking;
    }

    public void setCareTracking(CareTrackingEntity careTracking) {
        this.careTracking = careTracking;
    }

    public DoctorEntity getConsultedBy() {
        return consultedBy;
    }

    public void setConsultedBy(DoctorEntity consultedBy) {
        this.consultedBy = consultedBy;
    }

    public LocalDateTime getConsultedAt() {
        return consultedAt;
    }

    public void setConsultedAt(LocalDateTime consultedAt) {
        this.consultedAt = consultedAt;
    }

    public LocalDate getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CareTrackingTraceEntity that = (CareTrackingTraceEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
