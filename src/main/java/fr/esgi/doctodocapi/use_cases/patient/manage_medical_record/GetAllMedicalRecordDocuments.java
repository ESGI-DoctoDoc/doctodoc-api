package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.medical_record.MedicalRecordRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetAllMedicalRecordDocuments;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetAllMedicalRecordDocuments implements IGetAllMedicalRecordDocuments {
    private final MedicalRecordRepository medicalRecordRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final DocumentResponseMapper documentResponseMapper;

    public GetAllMedicalRecordDocuments(MedicalRecordRepository medicalRecordRepository, GetPatientFromContext getPatientFromContext, DocumentResponseMapper documentResponseMapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.documentResponseMapper = documentResponseMapper;
    }

    public final List<GetDocumentResponse> process(String type, int page, int size) {
        try {
            Patient patient = this.getPatientFromContext.get();
            List<Document> documents;

            if (type != null) {
                DocumentType documentType = DocumentType.fromValue(type);
                documents = this.medicalRecordRepository.getDocumentsByTypeAndPatientId(documentType.name(), patient.getId(), page, size);
            } else {
                documents = this.medicalRecordRepository.getDocumentsByPatientId(patient.getId(), page, size);
            }
            return documents.stream().map(this.documentResponseMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
