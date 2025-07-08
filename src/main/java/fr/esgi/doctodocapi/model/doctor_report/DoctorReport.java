package fr.esgi.doctodocapi.model.doctor_report;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DoctorReport {
    private UUID id;
    private UUID reporterId;
    private UUID doctorId;
    private String explanation;
    private DoctorReportStatus status;
    private LocalDateTime reportedAt;

    public DoctorReport(UUID id, UUID reporterId, UUID doctorId, String explanation, DoctorReportStatus status, LocalDateTime reportedAt) {
        this.id = id;
        this.reporterId = reporterId;
        this.doctorId = doctorId;
        this.explanation = explanation;
        this.status = status;
        this.reportedAt = reportedAt;
    }

    public static DoctorReport create(UUID userId, UUID doctorId, String explanation) {
        return new DoctorReport(
                UUID.randomUUID(),
                userId,
                doctorId,
                explanation,
                DoctorReportStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReporterId() {
        return reporterId;
    }

    public void setReporterId(UUID reporterId) {
        this.reporterId = reporterId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public DoctorReportStatus getStatus() {
        return status;
    }

    public void setStatus(DoctorReportStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorReport that = (DoctorReport) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
