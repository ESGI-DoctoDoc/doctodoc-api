package fr.esgi.doctodocapi.presentation.patient.manage_medical_record;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllMedicalRecordDocuments;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocumentMedicalRecordContent;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetMedicalRecordDocumentDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/medical-record")
public class GetDocumentController {
    private final IGetAllMedicalRecordDocuments getAllDocuments;
    private final IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent;
    private final IGetMedicalRecordDocumentDetail getDocumentDetail;

    public GetDocumentController(IGetAllMedicalRecordDocuments getAllDocuments, IGetDocumentMedicalRecordContent getDocumentMedicalRecordContent, IGetMedicalRecordDocumentDetail getDocumentDetail) {
        this.getAllDocuments = getAllDocuments;
        this.getDocumentMedicalRecordContent = getDocumentMedicalRecordContent;
        this.getDocumentDetail = getDocumentDetail;
    }

    @GetMapping("/documents")
    public List<GetDocumentResponse> getAll() {
        return this.getAllDocuments.process();
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
