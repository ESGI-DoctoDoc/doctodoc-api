package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientDocumentCareTrackingContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetPatientCareTrackingDocumentController {
    //    private final IGetAllMedicalRecordDocuments getAllDocuments;
    private final IGetPatientDocumentCareTrackingContent getPatientDocumentCareTrackingContent;
//    private final IGetMedicalRecordDocumentDetail getDocumentDetail;

    public GetPatientCareTrackingDocumentController(IGetPatientDocumentCareTrackingContent getPatientDocumentCareTrackingContent) {
        this.getPatientDocumentCareTrackingContent = getPatientDocumentCareTrackingContent;
    }


//    @GetMapping("/documents")
//    public List<GetDocumentResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String type) {
//        return this.getAllDocuments.process(type, page, size);
//    }

    @GetMapping("{careTrackingId}/documents/{id}")
    public GetDocumentResponse get(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientDocumentCareTrackingContent.process(careTrackingId, id);
    }

//    @GetMapping("/documents/detail/{id}")
//    public GetDocumentDetailResponse getDetail(@PathVariable UUID id) {
//        return this.getDocumentDetail.process(id);
//    }
}
