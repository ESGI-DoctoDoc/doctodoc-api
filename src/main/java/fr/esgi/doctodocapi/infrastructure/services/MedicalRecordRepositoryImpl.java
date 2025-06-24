package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentTracesEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentTracesJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MedicalRecordJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalRecordMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceMapper;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecord;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordNotFoundException;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {
    private final MedicalRecordJpaRepository medicalRecordJpaRepository;
    private final DocumentJpaRepository documentJpaRepository;
    private final DocumentTracesJpaRepository documentTracesJpaRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final DocumentMapper documentMapper;
    private final DocumentTraceMapper documentTraceMapper;
    private final DocumentTraceFacadeMapper documentTraceFacadeMapper;
    private final EntityManager entityManager;

    public MedicalRecordRepositoryImpl(MedicalRecordJpaRepository medicalRecordJpaRepository, DocumentJpaRepository documentJpaRepository, DocumentTracesJpaRepository documentTracesJpaRepository, MedicalRecordMapper medicalRecordMapper, DocumentMapper documentMapper, DocumentTraceMapper documentTraceMapper, DocumentTraceFacadeMapper documentTraceFacadeMapper, EntityManager entityManager) {
        this.medicalRecordJpaRepository = medicalRecordJpaRepository;
        this.documentJpaRepository = documentJpaRepository;
        this.documentTracesJpaRepository = documentTracesJpaRepository;
        this.medicalRecordMapper = medicalRecordMapper;
        this.documentMapper = documentMapper;
        this.documentTraceMapper = documentTraceMapper;
        this.documentTraceFacadeMapper = documentTraceFacadeMapper;
        this.entityManager = entityManager;
    }

    @Override
    public MedicalRecord getByPatientId(UUID patientId) throws MedicalConcernNotFoundException {
        MedicalRecordEntity medicalRecord = this.medicalRecordJpaRepository.findByPatientId(patientId).orElseThrow(MedicalRecordNotFoundException::new);
        List<Document> documents = medicalRecord.getDocuments().stream().map(document -> {
            List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
            return this.documentMapper.toDomain(document, traces);
        }).toList();
        return this.medicalRecordMapper.toDomain(medicalRecord, documents);
    }

    @Override
    public List<Document> getDocumentsByPatientId(UUID patientId, int page, int size) throws MedicalConcernNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentEntity> documents = this.documentJpaRepository.getAllByMedicalRecord_PatientIdOrderByUploadedAtDesc(patientId, pageable);
        return documents.getContent().stream().map(document -> {
            List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
            return this.documentMapper.toDomain(document, traces);
        }).toList();
    }

    @Override
    public List<Document> getDocumentsByTypeAndPatientId(String type, UUID patientId, int page, int size) throws MedicalConcernNotFoundException {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentEntity> documents = this.documentJpaRepository.getAllByMedicalRecord_PatientIdAndTypeContainsIgnoreCaseOrderByUploadedAtDesc(patientId, type, pageable);
        return documents.getContent().stream().map(document -> {
            List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
            return this.documentMapper.toDomain(document, traces);
        }).toList();
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        MedicalRecordEntity medicalRecordToSaved;

        if (medicalRecordJpaRepository.existsById(medicalRecord.id())) {
            medicalRecordToSaved = entityManager.getReference(MedicalRecordEntity.class, medicalRecord.id());
        } else {
            medicalRecordToSaved = this.medicalRecordMapper.toEntity(medicalRecord);
        }

        MedicalRecordEntity medicalRecordSaved = this.medicalRecordJpaRepository.save(medicalRecordToSaved);

        saveDocuments(medicalRecord.documents(), medicalRecordSaved);
    }

    private void saveDocuments(List<Document> documents, MedicalRecordEntity medicalRecord) {
        if (!documents.isEmpty()) {
            documents
                    .forEach(document -> {
                        DocumentEntity entity = this.documentMapper.toEntity(document, medicalRecord);
                        if (documentJpaRepository.existsById(document.getId())) {
                            entity.setId(document.getId());
                        }

                        this.documentJpaRepository.save(entity);
                        saveTraces(document);
                    });
        }
    }

    private void saveTraces(Document document) {
        List<DocumentTracesEntity> traces = document.getTraces().stream().map(trace -> this.documentTraceFacadeMapper.toEntity(document.getId(), trace)).toList();
        this.documentTracesJpaRepository.saveAll(traces);
    }
}
