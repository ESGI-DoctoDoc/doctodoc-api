package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IUpdatePatientCareTrackingDocument;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class UpdatePatientCareTrackingDocumentController {
    private final IUpdatePatientCareTrackingDocument updatePatientCareTrackingDocument;

    public UpdatePatientCareTrackingDocumentController(IUpdatePatientCareTrackingDocument updatePatientCareTrackingDocument) {
        this.updatePatientCareTrackingDocument = updatePatientCareTrackingDocument;
    }

    @PatchMapping("{careTrackingId}/documents/{id}")
    public GetDocumentResponse updateDocument(@PathVariable UUID careTrackingId, @PathVariable UUID id, @Valid @RequestBody SaveDocumentRequest saveDocumentRequest) {
        return this.updatePatientCareTrackingDocument.process(careTrackingId, id, saveDocumentRequest);
    }
}
