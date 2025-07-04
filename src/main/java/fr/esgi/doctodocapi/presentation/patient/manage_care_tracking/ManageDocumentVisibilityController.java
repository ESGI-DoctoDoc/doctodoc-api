package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IManageDocumentVisibility;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class ManageDocumentVisibilityController {
    private final IManageDocumentVisibility manageDocumentVisibility;

    public ManageDocumentVisibilityController(IManageDocumentVisibility manageDocumentVisibility) {
        this.manageDocumentVisibility = manageDocumentVisibility;
    }

    @PatchMapping("{careTrackingId}/documents/{id}/share")
    public void share(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        this.manageDocumentVisibility.share(careTrackingId, id);
    }


}
