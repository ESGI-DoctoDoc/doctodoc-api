package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
public class GetDocumentController {
    private final IGetAllDocuments getAllDocuments;
    private final IGetDocument getDocument;

    public GetDocumentController(IGetAllDocuments getAllDocuments, IGetDocument getDocument) {
        this.getAllDocuments = getAllDocuments;
        this.getDocument = getDocument;
    }

    @GetMapping("/documents")
    public List<GetDocumentResponse> getAll() {
        return this.getAllDocuments.process();
    }

    @GetMapping("/documents/{id}")
    public GetDocumentResponse get(@PathVariable UUID id) {
        return this.getDocument.process(id);
    }
}
