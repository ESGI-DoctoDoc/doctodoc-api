package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IDeletePatientCareTrackingDocument;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class DeletePatientCareTrackingDocumentController {
    private final IDeletePatientCareTrackingDocument deleteDocument;

    public DeletePatientCareTrackingDocumentController(IDeletePatientCareTrackingDocument deleteDocument) {
        this.deleteDocument = deleteDocument;
    }

    @DeleteMapping("{careTrackingId}/documents/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        this.deleteDocument.process(careTrackingId, id);
    }
}
