package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentDetailOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.care_tracking_responses.GetDocumentsOfCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetAllCareTrackingDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDocumentDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientDocumentCareTrackingContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetPatientCareTrackingDocumentController {
    private final IGetAllCareTrackingDocuments getAllDocuments;
    private final IGetPatientDocumentCareTrackingContent getPatientDocumentCareTrackingContent;
    private final IGetPatientCareTrackingDocumentDetail getPatientCareTrackingDocumentDetail;

    public GetPatientCareTrackingDocumentController(IGetAllCareTrackingDocuments getAllDocuments, IGetPatientDocumentCareTrackingContent getPatientDocumentCareTrackingContent, IGetPatientCareTrackingDocumentDetail getPatientCareTrackingDocumentDetail) {
        this.getAllDocuments = getAllDocuments;
        this.getPatientDocumentCareTrackingContent = getPatientDocumentCareTrackingContent;
        this.getPatientCareTrackingDocumentDetail = getPatientCareTrackingDocumentDetail;
    }

    @GetMapping("{id}/documents")
    public List<GetDocumentsOfCareTrackingResponse> getAll(@PathVariable UUID id) {
        return this.getAllDocuments.process(id);
    }

    @GetMapping("{careTrackingId}/documents/{id}")
    public GetDocumentsOfCareTrackingResponse get(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientDocumentCareTrackingContent.process(careTrackingId, id);
    }

    @GetMapping("{careTrackingId}/documents/detail/{id}")
    public GetDocumentDetailOfCareTrackingResponse getDetail(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientCareTrackingDocumentDetail.process(careTrackingId, id);
    }
}
