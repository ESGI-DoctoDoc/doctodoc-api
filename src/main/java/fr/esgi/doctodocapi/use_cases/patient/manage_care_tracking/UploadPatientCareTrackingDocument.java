package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IUploadPatientCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.http.HttpStatus;

import java.util.UUID;


public class UploadPatientCareTrackingDocument implements IUploadPatientCareTrackingDocument {
    private final GetPatientFromContext getPatientFromContext;
    private final CareTrackingRepository careTrackingRepository;
    private final FileStorageService fileStorageService;

    public UploadPatientCareTrackingDocument(GetPatientFromContext getPatientFromContext, CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService) {
        this.getPatientFromContext = getPatientFromContext;
        this.careTrackingRepository = careTrackingRepository;
        this.fileStorageService = fileStorageService;
    }

    public CreateMedicalRecordDocumentResponse createDocument(UUID id, SaveDocumentRequest saveDocumentRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(id, patient);

            CareTrackingDocument careTrackingDocument = CareTrackingDocument.create(saveDocumentRequest.filename(), saveDocumentRequest.type(), patient.getId(), careTracking.getId());
            careTracking.addDocument(careTrackingDocument);
            this.careTrackingRepository.save(careTracking);

            return new CreateMedicalRecordDocumentResponse(careTrackingDocument.getDocument().getId());

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    public GetUrlUploadResponse getPresignedUrlToUpload(UUID careTrackingId, UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);
            CareTrackingDocument document = careTracking.getById(id);
            String url = this.fileStorageService.getPresignedUrlToUpload(document.getDocument().getPath());
            return new GetUrlUploadResponse(url);
        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
