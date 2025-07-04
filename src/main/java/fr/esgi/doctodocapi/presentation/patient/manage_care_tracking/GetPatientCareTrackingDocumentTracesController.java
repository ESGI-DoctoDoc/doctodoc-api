package fr.esgi.doctodocapi.presentation.patient.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetMedicalRecordDocumentTracesResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IGetPatientCareTrackingDocumentTraces;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/care-trackings")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetPatientCareTrackingDocumentTracesController {
    private final IGetPatientCareTrackingDocumentTraces getPatientCareTrackingDocumentTraces;

    public GetPatientCareTrackingDocumentTracesController(IGetPatientCareTrackingDocumentTraces getPatientCareTrackingDocumentTraces) {
        this.getPatientCareTrackingDocumentTraces = getPatientCareTrackingDocumentTraces;
    }

    @GetMapping("{careTrackingId}/documents/{id}/traces")
    public List<GetMedicalRecordDocumentTracesResponse> get(@PathVariable UUID careTrackingId, @PathVariable UUID id) {
        return this.getPatientCareTrackingDocumentTraces.getAll(careTrackingId, id);
    }

}
