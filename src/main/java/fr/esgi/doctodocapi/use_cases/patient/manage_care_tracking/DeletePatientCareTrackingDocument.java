package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IDeletePatientCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeletePatientCareTrackingDocument implements IDeletePatientCareTrackingDocument {
    private final CareTrackingRepository careTrackingRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final IGetPatientFromContext getPatientFromContext;

    public DeletePatientCareTrackingDocument(CareTrackingRepository careTrackingRepository, DocumentRepository documentRepository, FileStorageService fileStorageService, GetPatientFromContext getPatientFromContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.getPatientFromContext = getPatientFromContext;
    }

    public void process(UUID careTrackingId, UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();

            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);
            CareTrackingDocument document = careTracking.getById(id);
            document.getDocument().delete(patient.getUserId());

            this.documentRepository.delete(document.getDocument());
            this.fileStorageService.delete(document.getDocument().getPath());

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
