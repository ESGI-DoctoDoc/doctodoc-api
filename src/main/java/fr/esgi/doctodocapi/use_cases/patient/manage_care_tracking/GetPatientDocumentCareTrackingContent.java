package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientDocumentCareTrackingContent;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetPatientDocumentCareTrackingContent implements IGetPatientDocumentCareTrackingContent {
    private final CareTrackingRepository careTrackingRepository;
    private final FileStorageService fileStorageService;
    private final IGetPatientFromContext getPatientFromContext;

    public GetPatientDocumentCareTrackingContent(CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.fileStorageService = fileStorageService;
        this.getPatientFromContext = getPatientFromContext;
    }

    public GetDocumentResponse process(UUID careTrackingId, UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);

            CareTrackingDocument document = careTracking.getById(id);
            String url = this.fileStorageService.getFile(document.getDocument().getPath());

            return new GetDocumentResponse(id, document.getDocument().getName(), document.getDocument().getType().getValue(), url);

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
