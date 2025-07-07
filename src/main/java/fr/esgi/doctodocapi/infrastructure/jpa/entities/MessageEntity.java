package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private DoctorEntity sender;

    @ManyToOne
    @JoinColumn(name = "care_tracking_id", nullable = false)
    private CareTrackingEntity careTracking;

    @Column(nullable = false, name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "files", columnDefinition = "uuid[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<UUID> files;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DoctorEntity getSender() {
        return sender;
    }

    public void setSender(DoctorEntity sender) {
        this.sender = sender;
    }

    public CareTrackingEntity getCareTracking() {
        return careTracking;
    }

    public void setCareTracking(CareTrackingEntity careTracking) {
        this.careTracking = careTracking;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public List<UUID> getFiles() {
        return files;
    }

    public void setFiles(List<UUID> files) {
        this.files = files;
    }
}
