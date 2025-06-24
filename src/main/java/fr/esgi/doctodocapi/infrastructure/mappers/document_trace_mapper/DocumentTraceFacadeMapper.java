package fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentTracesEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentTraceFacadeMapper {
    private final DocumentTraceMapper documentTraceMapper;
    private final EntityManager entityManager;

    public DocumentTraceFacadeMapper(DocumentTraceMapper documentTraceMapper, EntityManager entityManager) {
        this.documentTraceMapper = documentTraceMapper;
        this.entityManager = entityManager;
    }

    public DocumentTracesEntity toEntity(UUID documentId, DocumentTrace documentTrace) {
        UserEntity user = entityManager.getReference(UserEntity.class, documentTrace.userId());
        DocumentEntity document = entityManager.getReference(DocumentEntity.class, documentId);

        return this.documentTraceMapper.toEntity(documentTrace, user, document);
    }
}

