package fr.esgi.doctodocapi.model.doctor.care_tracking.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {
    private UUID id;
    private UUID senderId;
    private UUID careTrackingId;
    private String content;
    private LocalDateTime sentAt;

    public Message(UUID id, UUID senderId, UUID careTrackingId, String content, LocalDateTime sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.careTrackingId = careTrackingId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public static Message create(UUID senderId, UUID careTrackingId, String content) {
        return new Message(UUID.randomUUID(), senderId, careTrackingId, content, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getCareTrackingId() {
        return careTrackingId;
    }

    public void setCareTrackingId(UUID careTrackingId) {
        this.careTrackingId = careTrackingId;
    }
}
