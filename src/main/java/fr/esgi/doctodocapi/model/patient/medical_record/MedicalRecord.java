package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.document.Document;

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
    public void addDocument(Document document) {
        verifyIfFilenameAlreadyExist(document.getName());
        this.documents.add(document);
    }

    public void verifyIfFilenameAlreadyExist(String filename) {
        if (this.documents.stream().anyMatch(document -> Objects.equals(document.getName(), filename)))
            throw new DocumentWithSameNameAlreadyExist();
    }

    public void deleteDocument(Document document) {
        this.documents.remove(document);
    }

    @Override
    public List<Document> documents() {
        return List.copyOf(documents);
    }
}
