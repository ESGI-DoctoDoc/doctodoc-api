package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetDocumentResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IGetDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetDocument implements IGetDocument {
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public GetDocument(DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public GetDocumentResponse process(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            String url = this.fileStorageService.getFile(document.getPath());

            return new GetDocumentResponse(id, document.getName(), url);

        } catch (DoctorNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
