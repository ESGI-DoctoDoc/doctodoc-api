package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IUpdatePatientCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.IGetPatientFromContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UpdatePatientCareTrackingDocument implements IUpdatePatientCareTrackingDocument {
    private final IGetPatientFromContext getPatientFromContext;
    private final CareTrackingRepository careTrackingRepository;


    public UpdatePatientCareTrackingDocument(IGetPatientFromContext getPatientFromContext, CareTrackingRepository careTrackingRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.careTrackingRepository = careTrackingRepository;
    }

    public GetDocumentResponse process(UUID careTrackingId, UUID id, SaveDocumentRequest saveDocumentRequest) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);

            CareTrackingDocument document = careTracking.getById(id);

            CareTrackingDocument newDocument = CareTrackingDocument.copyOf(document);


            newDocument.getDocument().setId(document.getDocument().getId());
            newDocument.getDocument().update(saveDocumentRequest.filename(), DocumentType.fromValue(saveDocumentRequest.type()), patient.getUserId());

            careTracking.updateDocument(document, newDocument);

            this.careTrackingRepository.save(careTracking);
            return new GetDocumentResponse(newDocument.getDocument().getId(), newDocument.getDocument().getName(), document.getDocument().getType().getValue(), newDocument.getDocument().getPath());
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }

    }
}
