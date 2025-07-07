package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.DeleteCareTrackingDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.IDeleteDoctorCareTrackingDocument;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/doctors/care-trackings")
@PreAuthorize("hasRole('ROLE_DOCTOR')")
public class DeleteDoctorCareTrackingDocumentController {
    private final IDeleteDoctorCareTrackingDocument deleteDocument;

    public DeleteDoctorCareTrackingDocumentController(IDeleteDoctorCareTrackingDocument deleteDocument) {
        this.deleteDocument = deleteDocument;
    }

    @DeleteMapping("{careTrackingId}/documents/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public DeleteCareTrackingDocumentResponse delete(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.deleteDocument.execute(careTrackingId, id);
    }
}
