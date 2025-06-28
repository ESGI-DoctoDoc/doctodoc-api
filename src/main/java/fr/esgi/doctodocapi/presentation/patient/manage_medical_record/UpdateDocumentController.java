package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUpdateMedicalRecordDocument;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class UpdateDocumentController {
    private final IUpdateMedicalRecordDocument updateMedicalRecordDocument;

    public UpdateDocumentController(IUpdateMedicalRecordDocument updateMedicalRecordDocument) {
        this.updateMedicalRecordDocument = updateMedicalRecordDocument;
    }

    @PatchMapping("/documents/{id}")
    public GetDocumentResponse updateDocument(@PathVariable UUID id, @Valid @RequestBody SaveDocumentRequest saveDocumentRequest) {
        return this.updateMedicalRecordDocument.process(id, saveDocumentRequest);
    }
}
