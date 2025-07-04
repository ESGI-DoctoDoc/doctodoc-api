package fr.esgi.doctodocapi.model.document;

import fr.esgi.doctodocapi.model.document.trace.DocumentCreationTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentDeletionTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentUpdateTrace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Document {
    private UUID id;
    private String name;
    private String path;
    private DocumentType type;
    private UUID uploadedBy;
    private LocalDateTime uploadedAt;
    private List<DocumentTrace> traces;

    public Document(UUID id, String name, String path, DocumentType type, UUID uploadedBy, LocalDateTime uploadedAt, List<DocumentTrace> traces) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.traces = new ArrayList<>(traces);
    }

    public static Document init(String name, String url, DocumentType type, UUID uploadedBy) {
        DocumentTrace documentUploadTrace = new DocumentCreationTrace(uploadedBy);
        return new Document(
                UUID.randomUUID(),
                name,
                url,
                type,
                uploadedBy,
                LocalDateTime.now(),
                List.of(documentUploadTrace)
        );
    }

    public static Document copyOf(Document document) {
        return new Document(
                document.getId(),
                document.getName(),
                document.getPath(),
                document.getType(),
                document.getUploadedBy(),
                document.getUploadedAt(),
                document.traces
        );
    }

    public void delete(UUID userId) {
        this.traces.add(new DocumentDeletionTrace(userId));
    }

    public void update(String name, DocumentType type, UUID userId) {
        this.setName(name);
        this.setType(type);
        this.traces.add(new DocumentUpdateTrace(userId));
    }

    public void addTrace(DocumentTrace trace) {
        this.traces.add(trace);
    }

    public List<DocumentTrace> getTraces() {
        return List.copyOf(traces);
    }

    public void setTraces(List<DocumentTrace> traces) {
        this.traces = traces;
    }

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

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
