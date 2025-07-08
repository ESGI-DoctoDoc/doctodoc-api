package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.DeleteDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IDeleteDoctorDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.CannotDeleteDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class DeleteDoctorDocument implements IDeleteDoctorDocument {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DeleteDoctorDocument(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public DeleteDoctorDocumentResponse execute(UUID documentId) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Document document = this.documentRepository.getById(documentId);

            if (!Objects.equals(user.getId(), document.getUploadedBy())) {
                throw new CannotDeleteDocument();
            }

            this.documentRepository.delete(document);
            this.fileStorageService.delete(document.getPath());

           return new DeleteDoctorDocumentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
