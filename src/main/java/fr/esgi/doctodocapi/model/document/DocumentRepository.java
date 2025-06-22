package fr.esgi.doctodocapi.model.document;

import java.util.UUID;

public interface DocumentRepository {
    Document getById(UUID id) throws DocumentNotFoundException;

    void delete(Document document);
}
