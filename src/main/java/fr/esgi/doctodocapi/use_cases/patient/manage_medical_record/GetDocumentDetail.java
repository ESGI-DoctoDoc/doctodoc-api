package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentDetailResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocumentDetail;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetDocumentDetail implements IGetDocumentDetail {
    private final DocumentRepository documentRepository;

    public GetDocumentDetail(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public GetDocumentDetailResponse process(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            return new GetDocumentDetailResponse(
                    id,
                    document.getType().getValue(),
                    document.getName(),
                    document.getPath(),
                    document.getUploadedAt()
            );

        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
