package fr.esgi.doctodocapi.use_cases.patient.manage_medical_record;

import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserNotFoundException;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_medical_record.IDeleteMedicalRecordDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class DeleteMedicalRecordDocument implements IDeleteMedicalRecordDocument {
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;

    public DeleteMedicalRecordDocument(DocumentRepository documentRepository, FileStorageService fileStorageService, GetCurrentUserContext getCurrentUserContext, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
    }

    public void process(UUID id) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Document document = this.documentRepository.getById(id);
            document.delete(user.getId());

            this.documentRepository.delete(document);
            this.fileStorageService.delete(document.getPath());

        } catch (DocumentNotFoundException | UserNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
