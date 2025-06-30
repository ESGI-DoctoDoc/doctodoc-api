package fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.document.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUploadedDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.manage_doctor_account.document.GetUrlUploadForDoctorDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_doctor_account.document.IUploadDoctorOnboardingDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UploadDoctorOnboardingDocument implements IUploadDoctorOnboardingDocument {
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public UploadDoctorOnboardingDocument(UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public GetUploadedDoctorDocumentResponse execute(SaveDocumentRequest request) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);

            String prefixPath = "doctors/" + user.getId() + "/documents/";

            Document document = Document.init(
                    request.filename(),
                    prefixPath,
                    DocumentType.fromValue(request.type()),
                    user.getId()
            );
            document.setPath(prefixPath + document.getId());

            this.documentRepository.save(document);

            return new GetUploadedDoctorDocumentResponse(
                    document.getId(),
                    document.getName(),
                    document.getType().getValue(),
                    document.getPath()
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public GetUrlUploadForDoctorDocumentResponse getPresignedUrlToUpload(UUID documentId) {
        Document document = this.documentRepository.getById(documentId);
        String url = this.fileStorageService.getPresignedUrlToUpload(document.getPath());
        return new GetUrlUploadForDoctorDocumentResponse(url);
    }
}
