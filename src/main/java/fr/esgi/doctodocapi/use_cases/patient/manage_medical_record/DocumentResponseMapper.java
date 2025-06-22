package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.document.GetDocumentResponse;
import org.springframework.stereotype.Service;

@Service
public class DocumentResponseMapper {
    public GetDocumentResponse toDto(Document document) {
        return new GetDocumentResponse(document.getId(), document.getName(), document.getPath());
    }
}
