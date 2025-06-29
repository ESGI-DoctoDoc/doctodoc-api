package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetMedicalRecordDocumentTracesResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentTraces;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class GetMedicalRecordDocumentTracesController {
    private final IGetMedicalRecordDocumentTraces getMedicalRecordDocumentTraces;

    public GetMedicalRecordDocumentTracesController(IGetMedicalRecordDocumentTraces getMedicalRecordDocumentTraces) {
        this.getMedicalRecordDocumentTraces = getMedicalRecordDocumentTraces;
    }

    @GetMapping("/documents/{id}/traces")
    public List<GetMedicalRecordDocumentTracesResponse> get(@PathVariable UUID id) {
        return this.getMedicalRecordDocumentTraces.getAll(id);
    }

}
