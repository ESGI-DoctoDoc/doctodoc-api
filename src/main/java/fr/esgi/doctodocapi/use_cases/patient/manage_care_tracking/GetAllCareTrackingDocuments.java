package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetAllCareTrackingDocuments;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static java.util.Comparator.comparing;

public class GetAllCareTrackingDocuments implements IGetAllCareTrackingDocuments {
    private final CareTrackingRepository careTrackingRepository;
    private final DocumentCareTrackingResponseMapper documentCareTrackingResponseMapper;
    private final GetPatientFromContext getPatientFromContext;

    public GetAllCareTrackingDocuments(CareTrackingRepository careTrackingRepository, DocumentCareTrackingResponseMapper documentCareTrackingResponseMapper, GetPatientFromContext getPatientFromContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.documentCareTrackingResponseMapper = documentCareTrackingResponseMapper;
        this.getPatientFromContext = getPatientFromContext;
    }

    public List<GetDocumentsOfCareTrackingResponse> process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(id, patient);
            return careTracking.getDocuments()
                    .stream()
                    .sorted(comparing((CareTrackingDocument doc) -> doc.getDocument().getUploadedAt()).reversed())
                    .map(this.documentCareTrackingResponseMapper::toDto)
                    .toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
