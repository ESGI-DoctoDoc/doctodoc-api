package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "documents")
@SQLRestriction("deleted_at IS NULL")
public class DocumentEntity {
    @Id
    @Column(name = "document_id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "uploaded_by", nullable = false)
    private UUID uploadedBy;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecordEntity medicalRecord;

    @OneToMany(mappedBy = "document", cascade = CascadeType.PERSIST)
    private List<DocumentTracesEntity> traces;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "care_tracking_id")
    private CareTrackingEntity careTracking;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMedicalRecord(MedicalRecordEntity medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public UUID getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UUID uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public List<DocumentTracesEntity> getTraces() {
        return traces;
    }

    public void setTraces(List<DocumentTracesEntity> traces) {
        this.traces = traces;
    }

    public CareTrackingEntity getCareTracking() {
        return careTracking;
    }

    public void setCareTracking(CareTrackingEntity careTracking) {
        this.careTracking = careTracking;
    }

    public DoctorEntity getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEntity that = (DocumentEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
