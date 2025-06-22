package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllMedicalRecordDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocumentMedicalRecordContent;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
public class GetMedicalRecordDocumentController {
    private final IGetAllMedicalRecordDocuments getAllDocuments;
    private final IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent;
    private final IGetMedicalRecordDocumentDetail getDocumentDetail;

    public GetMedicalRecordDocumentController(IGetAllMedicalRecordDocuments getAllDocuments, IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent, IGetMedicalRecordDocumentDetail getDocumentDetail) {
        this.getAllDocuments = getAllDocuments;
        this.getDocumentMedicalRecordContent = getDocumentMedicalRecordContent;
        this.getDocumentDetail = getDocumentDetail;
    }

    @GetMapping("/documents")
    public List<GetDocumentResponse> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String type) {
        return this.getAllDocuments.process(type, page, size);
    }

    @GetMapping("/documents/{id}")
    public GetDocumentResponse get(@PathVariable UUID id) {
        return this.getDocumentMedicalRecordContent.process(id);
    }

    @GetMapping("/documents/detail/{id}")
    public GetDocumentDetailResponse getDetail(@PathVariable UUID id) {
        return this.getDocumentDetail.process(id);
    }
}
