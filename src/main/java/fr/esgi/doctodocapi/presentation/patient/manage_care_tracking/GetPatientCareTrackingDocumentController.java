package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetAllCareTrackingDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDocumentDetail;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientDocumentCareTrackingContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{careTrackingId}/documents")
    public List<GetDocumentResponse> getAll(@PathVariable UUID careTrackingId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String type) {
        return this.getAllDocuments.process(careTrackingId, type, page, size);
    }

    @GetMapping("{careTrackingId}/documents/{id}")
    public GetDocumentResponse get(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientDocumentCareTrackingContent.process(careTrackingId, id);
    }

    @GetMapping("{careTrackingId}/documents/detail/{id}")
    public GetDocumentDetailResponse getDetail(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientCareTrackingDocumentDetail.process(careTrackingId, id);
    }
}
