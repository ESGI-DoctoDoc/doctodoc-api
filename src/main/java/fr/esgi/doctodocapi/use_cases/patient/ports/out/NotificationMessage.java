package fr.esgi.doctodocapi.use_cases.patient.ports.out;

import java.util.Map;
import java.util.UUID;

public class NotificationMessage {
    private final UUID recipientId;
    private final String title;
    private final String body;
    private final Map<String, String> datas;

    public NotificationMessage(UUID recipientId, String title, String body, Map<String, String> datas) {
        this.recipientId = recipientId;
        this.title = title;
        this.body = body;
        this.datas = datas;
    }

    public NotificationMessage(UUID recipientId, String body, String title) {
        this.recipientId = recipientId;
        this.body = body;
        this.title = title;
        this.datas = null;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public UUID getRecipientId() {
        return recipientId;
    }
}
