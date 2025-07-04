package fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class CareTrackingTrace {
    private UUID id;
    private UUID consultedBy;
    private LocalDateTime consultedAt;

    public CareTrackingTrace(UUID id, UUID consultedBy, LocalDateTime consultedAt) {
        this.id = id;
        this.consultedBy = consultedBy;
        this.consultedAt = consultedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getConsultedBy() {
        return consultedBy;
    }

    public void setConsultedBy(UUID consultedBy) {
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
        CareTrackingTrace that = (CareTrackingTrace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
