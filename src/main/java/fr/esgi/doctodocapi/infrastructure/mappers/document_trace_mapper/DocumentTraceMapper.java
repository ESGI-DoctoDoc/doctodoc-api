package fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentTracesEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.document.trace.DocumentTraceType;
import org.springframework.stereotype.Service;

@Service
public class DocumentTraceMapper {

    public DocumentTrace toDomain(DocumentTracesEntity entity) {
        return new DocumentTrace(
                entity.getId(),
                DocumentTraceType.valueOf(entity.getType()),
                entity.getDescription(),
                entity.getBy().getId(),
                entity.getCreatedAt()
        );
    }

    public DocumentTracesEntity toEntity(DocumentTrace trace, UserEntity user, DocumentEntity document) {
        DocumentTracesEntity entity = new DocumentTracesEntity();
        entity.setId(trace.id());
        entity.setDescription(trace.description());
        entity.setCreatedAt(trace.date());
        entity.setType(trace.type().name());
        entity.setBy(user);
        entity.setDocument(document);
        return entity;
    }
}
