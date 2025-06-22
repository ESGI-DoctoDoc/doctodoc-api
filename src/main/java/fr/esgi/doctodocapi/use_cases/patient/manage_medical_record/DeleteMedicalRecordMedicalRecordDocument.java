package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IDeleteMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeleteMedicalRecordMedicalRecordDocument implements IDeleteMedicalRecordDocument {
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DeleteMedicalRecordMedicalRecordDocument(DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public void process(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            document.delete();

            this.documentRepository.delete(document);
            this.fileStorageService.delete(document.getPath());

        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
