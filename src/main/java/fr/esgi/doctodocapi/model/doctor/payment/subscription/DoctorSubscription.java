package fr.esgi.doctodocapi.model.doctor.payment.subscription;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DoctorSubscription {
    private UUID id;
    private UUID doctorId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;

    public DoctorSubscription(UUID id, UUID doctorId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt) {
        this.id = id;
        this.doctorId = doctorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }

    public static DoctorSubscription create(UUID doctorId, LocalDateTime startDate) {
        return new DoctorSubscription(
                UUID.randomUUID(),
                doctorId,
                startDate,
                startDate.plusYears(1),
                LocalDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DoctorSubscription that = (DoctorSubscription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
