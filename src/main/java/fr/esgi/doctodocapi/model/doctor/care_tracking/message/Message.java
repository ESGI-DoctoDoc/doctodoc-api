package fr.esgi.doctodocapi.model.doctor.care_tracking.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Message {
    private UUID id;
    private UUID senderId;
    private UUID careTrackingId;
    private String content;
    private List<UUID> files;
    private LocalDateTime sentAt;

    public Message(UUID id, UUID senderId, UUID careTrackingId, String content, List<UUID> files, LocalDateTime sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.careTrackingId = careTrackingId;
        this.content = content;
        this.files = files != null ? files : new ArrayList<>();
        this.sentAt = sentAt;
    }

    public static Message create(UUID senderId, UUID careTrackingId, String content, List<UUID> files) {
        return new Message(
                UUID.randomUUID(),
                senderId,
                careTrackingId,
                content,
                files,
                LocalDateTime.now()
        );
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

    public List<UUID> getFiles() {
        return files;
    }

    public void setFiles(List<UUID> files) {
        this.files = files;
    }
}
