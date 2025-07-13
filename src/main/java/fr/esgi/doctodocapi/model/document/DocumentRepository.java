package fr.esgi.doctodocapi.model.document;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository {
    Document getById(UUID id) throws DocumentNotFoundException;
    List<Document> getByDoctorId(UUID doctorId) throws DocumentNotFoundException;

    void delete(Document document);
    void save(Document document);
}
