package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record MedicalRecord(UUID id, UUID patientId, List<Document> documents) {
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
    public Document getById(UUID id) {
        return this.documents
                .stream()
                .filter(document -> document.getId().equals(id))
                .findFirst()
                .orElseThrow(DocumentNotFoundInMedicalRecordException::new);
    }

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

    private void verifyIfFilenameAlreadyExist(String filename) {
        if (this.documents.stream().anyMatch(document -> Objects.equals(document.getName(), filename)))
            throw new DocumentWithSameNameAlreadyExist();
    }

    private void verifyIfFilenameAlreadyExist(String filename, Document excludedDocument) {
        boolean exists = this.documents.stream()
                .filter(doc -> !doc.equals(excludedDocument))
                .anyMatch(doc -> Objects.equals(doc.getName(), filename));

        if (exists) {
            throw new DocumentWithSameNameAlreadyExist();
        }
    }


    @Override
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

}
