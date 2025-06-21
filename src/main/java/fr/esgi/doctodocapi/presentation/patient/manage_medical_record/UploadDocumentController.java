package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetUrlUploadResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUploadDocument;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients/medical-record")
public class UploadDocumentController {
    private final IUploadDocument uploadDocument;

    public UploadDocumentController(IUploadDocument uploadDocument) {
        this.uploadDocument = uploadDocument;
    }

    @GetMapping("/upload-url/{filename}")
    public GetUrlUploadResponse uploadUrl(@PathVariable String filename) {
        return this.uploadDocument.getPresignedUrlToUpload(filename);
    }

    @PostMapping("/documents")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void create(@Valid @RequestBody SaveDocumentRequest saveDocumentRequest) {
        this.uploadDocument.createDocument(saveDocumentRequest);
    }
}
