package fr.esgi.doctodocapi.model.document.trace;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class DocumentTrace {
    private final UUID id;
    private final DocumentTraceType type;
    private final String description;
    private final UUID userId;
    private final LocalDateTime date;


    public DocumentTrace(UUID id, DocumentTraceType type, String description, UUID userId, LocalDateTime date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.date = date;
    }

    protected DocumentTrace(DocumentTraceType type, String description, UUID userId) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.date = LocalDateTime.now();
    }

    public UUID id() {
        return id;
    }

    public DocumentTraceType type() {
        return type;
    }

    public String description() {
        return description;
    }

    public UUID userId() {
        return userId;
    }

    public LocalDateTime date() {
        return date;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentTrace that = (DocumentTrace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

