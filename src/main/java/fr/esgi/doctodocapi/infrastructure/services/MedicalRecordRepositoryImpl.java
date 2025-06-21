package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MedicalRecordJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalRecordMapper;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordNotFoundException;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final MedicalRecordJpaRepository medicalRecordJpaRepository;
    private final DocumentJpaRepository documentJpaRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final DocumentMapper documentMapper;

    public MedicalRecordRepositoryImpl(MedicalRecordJpaRepository medicalRecordJpaRepository, DocumentJpaRepository documentJpaRepository, MedicalRecordMapper medicalRecordMapper, DocumentMapper documentMapper) {
        this.medicalRecordJpaRepository = medicalRecordJpaRepository;
        this.documentJpaRepository = documentJpaRepository;
        this.medicalRecordMapper = medicalRecordMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public MedicalRecord getByPatientId(UUID patientId) throws MedicalConcernNotFoundException {
        MedicalRecordEntity medicalRecord = this.medicalRecordJpaRepository.findByPatientId(patientId).orElseThrow(MedicalRecordNotFoundException::new);
        List<Document> documents = medicalRecord.getDocuments().stream().map(documentMapper::toDomain).toList();
        return this.medicalRecordMapper.toDomain(medicalRecord, documents);
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        if (medicalRecordJpaRepository.existsById(medicalRecord.id())) {
            update(medicalRecord);
        } else {
            MedicalRecordEntity medicalRecordToSaved = this.medicalRecordMapper.toEntity(medicalRecord);
            MedicalRecordEntity medicalRecordSaved = this.medicalRecordJpaRepository.save(medicalRecordToSaved);

            List<DocumentEntity> documents = medicalRecord.documents().stream()
                    .map(document -> this.documentMapper.toEntity(document, medicalRecordSaved))
                    .toList();

            if (!documents.isEmpty()) {
                this.documentJpaRepository.saveAll(documents);
            }
        }


    }

    private void update(MedicalRecord medicalRecord) {
        MedicalRecordEntity medicalRecordEntity = this.medicalRecordJpaRepository.findById(medicalRecord.id()).orElseThrow(MedicalConcernNotFoundException::new);

        List<DocumentEntity> documents = medicalRecord.documents().stream()
                .map(document -> {
                    DocumentEntity entity = this.documentMapper.toEntity(document, medicalRecordEntity);
                    if (documentJpaRepository.existsById(document.getId())) {
                        entity.setId(document.getId());
                    }

                    return entity;

                })
                .toList();

        this.documentJpaRepository.saveAll(documents);
    }
}
