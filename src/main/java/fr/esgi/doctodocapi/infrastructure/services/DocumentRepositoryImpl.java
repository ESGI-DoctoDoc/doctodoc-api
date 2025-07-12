package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DocumentTracesEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentTracesJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceMapper;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class DocumentRepositoryImpl implements DocumentRepository {
    private final DocumentTracesJpaRepository documentTracesJpaRepository;
    private final DocumentJpaRepository documentJpaRepository;
    private final DocumentMapper documentMapper;
    private final DocumentTraceMapper documentTraceMapper;
    private final DocumentTraceFacadeMapper documentTraceFacadeMapper;

    public DocumentRepositoryImpl(DocumentTracesJpaRepository documentTracesJpaRepository, DocumentJpaRepository documentJpaRepository, DocumentMapper documentMapper, DocumentTraceMapper documentTraceMapper, DocumentTraceFacadeMapper documentTraceFacadeMapper) {
        this.documentTracesJpaRepository = documentTracesJpaRepository;
        this.documentJpaRepository = documentJpaRepository;
        this.documentMapper = documentMapper;
        this.documentTraceMapper = documentTraceMapper;
        this.documentTraceFacadeMapper = documentTraceFacadeMapper;
    }

    @Override
    public Document getById(UUID id) throws DocumentNotFoundException {
        DocumentEntity document = this.documentJpaRepository.findById(id).orElseThrow(DocumentNotFoundException::new);
        List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
        return this.documentMapper.toDomain(document, traces);
    }

    @Override
    public void delete(Document document) {
        DocumentEntity documentToDelete = this.documentJpaRepository.findById(document.getId()).orElseThrow(DocumentNotFoundException::new);
        documentToDelete.setDeletedAt(LocalDateTime.now());

        this.documentJpaRepository.save(documentToDelete);

        // save trace
        List<DocumentTracesEntity> traces = document.getTraces().stream().map(trace -> this.documentTraceFacadeMapper.toEntity(document.getId(), trace)).toList();
        this.documentTracesJpaRepository.saveAll(traces);

        // todo delete permissions
    }

    @Override
    public void save(Document document) {
        DocumentEntity entity = this.documentMapper.toEntity(document, null, null, null);

        if (this.documentJpaRepository.existsById(document.getId())) {
            entity.setId(document.getId());
        }

        this.documentJpaRepository.save(entity);
        saveTraces(document);
    }

    @Override
    public List<Document> getByDoctorId(UUID doctorId) throws DocumentNotFoundException {
        List<DocumentEntity> entities = this.documentJpaRepository.findAllByUploadedBy(doctorId);

        if (entities.isEmpty()) {
            throw new DocumentNotFoundException();
        }

        return entities.stream()
                .map(entity -> {
                    List<DocumentTrace> traces = entity.getTraces().stream()
                            .map(this.documentTraceMapper::toDomain)
                            .toList();
                    return this.documentMapper.toDomain(entity, traces);
                })
                .toList();
    }

    private void saveTraces(Document document) {
        List<DocumentTracesEntity> traces = document.getTraces().stream().map(trace -> this.documentTraceFacadeMapper.toEntity(document.getId(), trace)).toList();
        this.documentTracesJpaRepository.saveAll(traces);
    }
}
