package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IUpdateMedicalRecordDocument;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
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
