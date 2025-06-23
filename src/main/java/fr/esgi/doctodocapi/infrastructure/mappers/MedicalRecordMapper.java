package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordMapper {

    public MedicalRecord toDomain(MedicalRecordEntity entity, List<Document> documents) {
        return new MedicalRecord(
                entity.getId(),
                entity.getPatientId(),
                documents
        );
    }

    public MedicalRecordEntity toEntity(MedicalRecord medicalRecord) {
        MedicalRecordEntity entity = new MedicalRecordEntity();
        entity.setPatientId(medicalRecord.patientId());
        return entity;
    }
}
