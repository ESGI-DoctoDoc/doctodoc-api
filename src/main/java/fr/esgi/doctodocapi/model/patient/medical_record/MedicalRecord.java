package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class MedicalRecord {
    private final UUID id;
    private final UUID patientId;
    private final List<Document> documents;

    public MedicalRecord(UUID id, UUID patientId) {
        this.id = id;
        this.patientId = patientId;
        this.documents = null;
    }

    public MedicalRecord(UUID id, UUID patientId, List<Document> documents) {
        this.id = id;
        this.patientId = patientId;
        this.documents = new ArrayList<>(documents);
    }

    public static MedicalRecord init(UUID patientId) {
        return new MedicalRecord(
                UUID.randomUUID(),
                patientId,
                List.of()
        );
    }

    // manage document
    public void addDocument(Document document) {
        verifyIfFilenameAlreadyExist(document.getName());
        this.documents.add(document);
    }

    public void updateDocument(Document oldDocument, Document newDocument) {
        verifyIfFilenameAlreadyExist(newDocument.getName(), oldDocument);
        if (!this.documents.contains(oldDocument)) throw new DocumentNotFoundException();
        this.documents.remove(oldDocument);
        this.documents.add(newDocument);
    }

    public void verifyIfFilenameAlreadyExist(String filename) {
        if (this.documents.stream().anyMatch(document -> Objects.equals(document.getName(), filename)))
            throw new DocumentWithSameNameAlreadyExist();
    }

    public void verifyIfFilenameAlreadyExist(String filename, Document excludedDocument) {
        boolean exists = this.documents.stream()
                .filter(doc -> !doc.equals(excludedDocument))
                .anyMatch(doc -> Objects.equals(doc.getName(), filename));

        if (exists) {
            throw new DocumentWithSameNameAlreadyExist();
        }
    }


    public void deleteDocument(Document document) {
        this.documents.remove(document);
    }

    public List<Document> documents() {
        return List.copyOf(documents);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MedicalRecord that = (MedicalRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", documents=" + documents +
                '}';
    }

    public UUID id() {
        return id;
    }

    public UUID patientId() {
        return patientId;
    }

}
