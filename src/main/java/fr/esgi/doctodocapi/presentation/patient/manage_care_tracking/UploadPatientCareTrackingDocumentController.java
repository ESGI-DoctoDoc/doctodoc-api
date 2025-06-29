package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IUploadPatientCareTrackingDocument;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class UploadPatientCareTrackingDocumentController {
    private final IUploadPatientCareTrackingDocument uploadDocument;

    public UploadPatientCareTrackingDocumentController(IUploadPatientCareTrackingDocument uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    @GetMapping("/upload-url/{id}")
    public GetUrlUploadResponse uploadUrl(@PathVariable UUID id) {
        return this.uploadDocument.getPresignedUrlToUpload(id);
    }

    @PostMapping("{id}/documents")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateMedicalRecordDocumentResponse create(@PathVariable UUID id, @Valid @RequestBody SaveDocumentRequest saveDocumentRequest) {
        return this.uploadDocument.createDocument(id, saveDocumentRequest);
    }
}
