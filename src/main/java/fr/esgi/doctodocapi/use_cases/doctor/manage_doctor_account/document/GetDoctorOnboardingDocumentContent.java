package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetDocumentForDoctorOnboardingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IGetDoctorOnboardingDocumentContent;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetDoctorOnboardingDocumentContent implements IGetDoctorOnboardingDocumentContent {
    private final FileStorageService fileStorageService;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public GetDoctorOnboardingDocumentContent(FileStorageService fileStorageService, DocumentRepository documentRepository, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext) {
        this.fileStorageService = fileStorageService;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public GetDocumentForDoctorOnboardingResponse execute(UUID documentId) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            Document document = this.documentRepository.getById(documentId);

            if (!document.getUploadedBy().equals(user.getId())) {
                throw new ApiException(HttpStatus.FORBIDDEN, "document.forbidden", "Vous n'avez pas accès à ce document.");
            }

            String url = this.fileStorageService.getFile(document.getPath());

            return new GetDocumentForDoctorOnboardingResponse(
                    document.getId(),
                    document.getName(),
                    document.getType().getValue(),
                    url
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}