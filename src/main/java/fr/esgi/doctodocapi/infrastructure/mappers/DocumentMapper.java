package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentMapper {

    public Document toDomain(DocumentEntity entity, List<DocumentTrace> traces) {
        return new Document(
                entity.getId(),
                entity.getName(),
                entity.getPath(),
                DocumentType.valueOf(entity.getType()),
                entity.getUploadedBy(),
                entity.getUploadedAt(),
                traces,
                List.of()
        );
    }

    public DocumentEntity toEntity(Document document, MedicalRecordEntity medicalRecordEntity, CareTrackingEntity careTrackingEntity, DoctorEntity doctorEntity) {
        DocumentEntity entity = new DocumentEntity();
        entity.setId(document.getId());
        entity.setName(document.getName());
        entity.setType(document.getType().name());
        entity.setPath(document.getPath());
        entity.setUploadedBy(document.getUploadedBy());
        entity.setUploadedAt(document.getUploadedAt());
        entity.setMedicalRecord(medicalRecordEntity);
        entity.setCareTracking(careTrackingEntity);
        entity.setDoctor(doctorEntity);

        return entity;
    }
}
