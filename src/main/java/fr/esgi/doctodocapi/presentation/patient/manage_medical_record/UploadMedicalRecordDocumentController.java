package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.CreateMedicalRecordDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadMedicalRecordDocument;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class UploadMedicalRecordDocumentController {
    private final IUploadMedicalRecordDocument uploadDocument;

    public UploadMedicalRecordDocumentController(IUploadMedicalRecordDocument uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    @GetMapping("/upload-url/{id}")
    public GetUrlUploadResponse uploadUrl(@PathVariable UUID id) {
        return this.uploadDocument.getPresignedUrlToUpload(id);
    }

    @PostMapping("/documents")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CreateMedicalRecordDocumentResponse create(@Valid @RequestBody SaveDocumentRequest saveDocumentRequest) {
        return this.uploadDocument.createDocument(saveDocumentRequest);
    }
}
