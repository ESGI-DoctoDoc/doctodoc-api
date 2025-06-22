package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentMapper {

    public Document toDomain(DocumentEntity entity) {
        return new Document(
                entity.getId(),
                entity.getName(),
                entity.getPath(),
                DocumentType.valueOf(entity.getType()),
                entity.getUploadedBy(),
                entity.getUploadedAt(),
                List.of(),
                List.of()
        );
    }

    public DocumentEntity toEntity(Document document, MedicalRecordEntity medicalRecordEntity) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(document.getId());
        entity.setName(document.getName());
        entity.setType(document.getType().name());
        entity.setPath(document.getPath());
        entity.setMedicalRecord(medicalRecordEntity);
        entity.setUploadedBy(document.getUploadedBy());
        entity.setUploadedAt(document.getUploadedAt());
        return entity;
    }
}
