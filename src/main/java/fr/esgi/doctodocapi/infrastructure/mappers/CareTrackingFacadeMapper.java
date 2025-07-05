package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceMapper;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.care_tracking_trace.CareTrackingTrace;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.patient.Patient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareTrackingFacadeMapper {
    private final CareTrackingMapper careTrackingMapper;
    private final PatientMapper patientMapper;
    private final CareTrackingTraceMapper careTrackingTraceMapper;
    private final DocumentMapper documentMapper;
    private final DocumentTraceMapper documentTraceMapper;

    public CareTrackingFacadeMapper(CareTrackingMapper careTrackingMapper, PatientMapper patientMapper, CareTrackingTraceMapper careTrackingTraceMapper, DocumentMapper documentMapper, DocumentTraceMapper documentTraceMapper) {
        this.careTrackingMapper = careTrackingMapper;
        this.patientMapper = patientMapper;
        this.careTrackingTraceMapper = careTrackingTraceMapper;
        this.documentMapper = documentMapper;
        this.documentTraceMapper = documentTraceMapper;
    }

    public CareTracking mapCareTrackingToDomain(CareTrackingEntity entity) {
        Patient patient = this.patientMapper.toDomain(entity.getPatient());

        List<CareTrackingTrace> traces = entity.getCareTrackingTraces()
                .stream()
                .map(careTrackingTraceMapper::toDomain)
                .toList();

        List<CareTrackingDocument> documents = entity.getDocuments().stream().map(documentEntity -> {
            boolean isShared = documentEntity.isShared();
            Document document = documentMapper.toDomain(documentEntity, documentEntity.getTraces().stream().map(documentTraceMapper::toDomain).toList());
            return new CareTrackingDocument(document, isShared);
        }).toList();

        return this.careTrackingMapper.toDomain(entity, patient, traces, documents);
    }
}
