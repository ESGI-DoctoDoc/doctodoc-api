package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class DocumentRepositoryImpl implements DocumentRepository {
    private final DocumentJpaRepository documentJpaRepository;
    private final DocumentMapper documentMapper;

    public DocumentRepositoryImpl(DocumentJpaRepository documentJpaRepository, DocumentMapper documentMapper) {
        this.documentJpaRepository = documentJpaRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public Document getById(UUID id) throws DocumentNotFoundException {
        DocumentEntity document = this.documentJpaRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        return this.documentMapper.toDomain(document);
    }

    @Override
    public void delete(UUID id) {
        DocumentEntity documentToDelete = this.documentJpaRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        documentToDelete.setDeletedAt(LocalDateTime.now());
        this.documentJpaRepository.save(documentToDelete);
        // todo delete traces
        // todo delete permissions
    }
}
