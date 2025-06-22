package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IDeleteMedicalRecordDocument;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
public class DeleteDocumentController {
    private final IDeleteMedicalRecordDocument deleteDocument;

    public DeleteDocumentController(IDeleteMedicalRecordDocument deleteDocument) {
        this.deleteDocument = deleteDocument;
    }

    @DeleteMapping("/documents/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        this.deleteDocument.process(id);
    }
}
