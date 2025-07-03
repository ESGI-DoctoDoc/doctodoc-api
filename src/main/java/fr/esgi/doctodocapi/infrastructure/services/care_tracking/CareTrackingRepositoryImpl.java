package fr.esgi.doctodocapi.infrastructure.services.care_tracking;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.*;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.CareTrackingJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DocumentTracesJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.CareTrackingMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.DocumentMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceMapper;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingNotFoundException;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.trace.DocumentTrace;
import fr.esgi.doctodocapi.model.patient.Patient;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class CareTrackingRepositoryImpl implements CareTrackingRepository {
    private final CareTrackingJpaRepository careTrackingJpaRepository;
    private final CareTrackingMapper careTrackingMapper;
    private final EntityManager entityManager;
    private final CareTrackingFacadeMapper careTrackingFacadeMapper;
    private final DocumentMapper documentMapper;
    private final DocumentJpaRepository documentJpaRepository;
    private final DocumentTraceFacadeMapper documentTraceFacadeMapper;
    private final DocumentTraceMapper documentTraceMapper;
    private final DocumentTracesJpaRepository documentTracesJpaRepository;

    public CareTrackingRepositoryImpl(CareTrackingJpaRepository careTrackingJpaRepository, CareTrackingMapper careTrackingMapper, EntityManager entityManager, CareTrackingFacadeMapper careTrackingFacadeMapper, DocumentMapper documentMapper, DocumentJpaRepository documentJpaRepository, DocumentTraceFacadeMapper documentTraceFacadeMapper, DocumentTraceMapper documentTraceMapper, DocumentTracesJpaRepository documentTracesJpaRepository) {
        this.careTrackingJpaRepository = careTrackingJpaRepository;
        this.careTrackingMapper = careTrackingMapper;
        this.entityManager = entityManager;
        this.careTrackingFacadeMapper = careTrackingFacadeMapper;
        this.documentMapper = documentMapper;
        this.documentJpaRepository = documentJpaRepository;
        this.documentTraceFacadeMapper = documentTraceFacadeMapper;
        this.documentTraceMapper = documentTraceMapper;
        this.documentTracesJpaRepository = documentTracesJpaRepository;
    }

    // doctor

    @Override
    public UUID save(CareTracking careTracking) {
        DoctorEntity doctor = this.entityManager.getReference(DoctorEntity.class, careTracking.getCreatorId());
        PatientEntity patient = this.entityManager.getReference(PatientEntity.class, careTracking.getPatient().getId());

        CareTrackingEntity entity;

        if (careTrackingJpaRepository.existsById(careTracking.getId())) {
            entity = this.careTrackingJpaRepository.findById(careTracking.getId())
                    .orElseThrow(CareTrackingNotFoundException::new);
        } else {
            entity = this.careTrackingMapper.toEntity(careTracking, patient);
        }

        List<AppointmentEntity> appointmentEntities = new ArrayList<>(
                careTracking.getAppointmentsId()
                        .stream()
                        .map(id -> this.entityManager.getReference(AppointmentEntity.class, id))
                        .toList()
        );

        entity.setAppointments(appointmentEntities);
        entity.setCreator(doctor);

        CareTrackingEntity savedEntity = this.careTrackingJpaRepository.save(entity);

        saveDocuments(careTracking.getDocuments(), savedEntity);

        return savedEntity.getId();
    }

    private void saveDocuments(List<Document> documents, CareTrackingEntity careTrackingEntity) {
        if (!documents.isEmpty()) {
            documents
                    .forEach(document -> {
                        DocumentEntity entity = this.documentMapper.toEntity(document, null, careTrackingEntity, null);
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

    @Override
    public List<CareTracking> findAll(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.careTrackingJpaRepository.findAllByCreator_Id(doctorId, pageable)
                .stream()
                .map(careTrackingFacadeMapper::mapCareTrackingToDomain)
                .toList();
    }

    @Override
    public CareTracking getById(UUID id) throws CareTrackingNotFoundException {
        CareTrackingEntity careTrackingEntity = this.careTrackingJpaRepository.findById(id).orElseThrow(CareTrackingNotFoundException::new);
        return careTrackingFacadeMapper.mapCareTrackingToDomain(careTrackingEntity);
    }

    @Override
    public CareTracking getByIdAndPatient(UUID careTrackingId, Patient patient) throws CareTrackingNotFoundException {
        CareTrackingEntity entity = this.careTrackingJpaRepository.findByIdAndPatient_Id(careTrackingId, patient.getId()).orElseThrow(CareTrackingNotFoundException::new);
        return this.careTrackingFacadeMapper.mapCareTrackingToDomain(entity);
    }

    @Override
    public CareTracking getByIdAndDoctor(UUID careTrackingId, Doctor doctor) throws CareTrackingNotFoundException {
        CareTrackingEntity entity = this.careTrackingJpaRepository.findByIdAndDoctorAccess(careTrackingId, doctor.getId()).orElseThrow(CareTrackingNotFoundException::new);
        return this.careTrackingFacadeMapper.mapCareTrackingToDomain(entity);
    }


    // patient
    @Override
    public List<CareTracking> findAllOpenedByPatientId(UUID patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return this.careTrackingJpaRepository.findAllByPatient_IdOrderByCreatedAtDesc(patientId, pageable)
                .stream()
                .map(careTrackingFacadeMapper::mapCareTrackingToDomain)
                .toList();
    }

    @Override
    public List<CareTracking> findAllOpenedByPatientId(UUID patientId) {
        return this.careTrackingJpaRepository.findAllByPatient_IdAndClosedAtNullOrderByCreatedAtDesc(patientId)
                .stream()
                .map(careTrackingFacadeMapper::mapCareTrackingToDomain)
                .toList();
    }

    @Override
    public List<Document> getDocumentsByCareTrackingIdAndType(UUID id, String type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentEntity> documents = this.documentJpaRepository.getAllByCareTracking_IdAndTypeOrderByUploadedAtDesc(id, type, pageable);
        return documents.getContent().stream().map(document -> {
            List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
            return this.documentMapper.toDomain(document, traces);
        }).toList();
    }

    @Override
    public List<Document> getDocumentsByPatientId(UUID id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentEntity> documents = this.documentJpaRepository.getAllByCareTracking_IdOrderByUploadedAtDesc(id, pageable);
        return documents.getContent().stream().map(document -> {
            List<DocumentTrace> traces = document.getTraces().stream().map(this.documentTraceMapper::toDomain).toList();
            return this.documentMapper.toDomain(document, traces);
        }).toList();
    }
}
