package fr.esgi.doctodocapi.model.notification;

import java.time.LocalDateTime;
import java.util.UUID;

public class Notification {
    private final UUID id;
    private final String title;
    private final String content;
    private final LocalDateTime sendAt;
    private boolean isRead;

    public Notification(UUID id, String title, String content, boolean isRead, LocalDateTime sendAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.sendAt = sendAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }
}
