package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetUrlUploadForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IUploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class UploadCareTrackingDocumentController {
    private final IUploadCareTrackingDocument uploadCareTrackingDocument;

    public UploadCareTrackingDocumentController(IUploadCareTrackingDocument uploadCareTrackingDocument) {
        this.uploadCareTrackingDocument = uploadCareTrackingDocument;
    }

    @GetMapping("care-tracking/upload-url/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GetUrlUploadForCareTrackingResponse getPresignedUrl(@PathVariable UUID id) {
        return this.uploadCareTrackingDocument.getPresignedUrlToUpload(id);
    }

    @PostMapping("care-tracking/{id}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public GetDocumentForCareTrackingResponse createDocument(@PathVariable UUID id, @RequestBody SaveDocumentRequest request) {
        return this.uploadCareTrackingDocument.execute(id, request);
    }
}
