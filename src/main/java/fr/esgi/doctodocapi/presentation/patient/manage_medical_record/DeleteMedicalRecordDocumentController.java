package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IDeleteMedicalRecordDocument;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
@PreAuthorize("hasRole('ROLE_PATIENT')")
public class DeleteMedicalRecordDocumentController {
    private final IDeleteMedicalRecordDocument deleteDocument;

    public DeleteMedicalRecordDocumentController(IDeleteMedicalRecordDocument deleteDocument) {
        this.deleteDocument = deleteDocument;
    }

    @DeleteMapping("/documents/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        this.deleteDocument.process(id);
    }
}
