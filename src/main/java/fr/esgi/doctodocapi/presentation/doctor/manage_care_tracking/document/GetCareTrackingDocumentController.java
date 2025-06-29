package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IGetCareTrackingDocumentContent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("doctors")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class GetCareTrackingDocumentController {
    private final IGetCareTrackingDocumentContent getCareTrackingDocumentContent;

    public GetCareTrackingDocumentController(IGetCareTrackingDocumentContent getCareTrackingDocumentContent) {
        this.getCareTrackingDocumentContent = getCareTrackingDocumentContent;
    }

    @GetMapping("care-tracking/{careTrackingId}/documents/{documentId}")
    public GetDocumentForCareTrackingResponse getDocumentContent(@PathVariable UUID careTrackingId, @PathVariable UUID documentId) {
        return this.getCareTrackingDocumentContent.execute(careTrackingId, documentId);
    }
}
