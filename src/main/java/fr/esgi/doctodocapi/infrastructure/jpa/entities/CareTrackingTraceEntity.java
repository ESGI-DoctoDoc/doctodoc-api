package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "care_tracking_traces")
public class CareTrackingTraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "care_tracking_trace_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "care_tracking_id", nullable = false)
    private CareTrackingEntity careTracking;

    @ManyToOne
    @JoinColumn(name = "consulted_by", nullable = false)
    private DoctorEntity consultedBy;

    @Column(name = "consulted_at")
    private LocalDateTime consultedAt;

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
