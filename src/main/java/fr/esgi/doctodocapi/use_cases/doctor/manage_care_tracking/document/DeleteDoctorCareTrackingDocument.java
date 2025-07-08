package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.DeleteCareTrackingDocumentResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.IDeleteDoctorCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.exceptions.CannotDeleteDocument;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

public class DeleteDoctorCareTrackingDocument implements IDeleteDoctorCareTrackingDocument {
    private final GetCurrentUserContext getCurrentUserContext;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final CareTrackingRepository careTrackingRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    public DeleteDoctorCareTrackingDocument(GetCurrentUserContext getCurrentUserContext, UserRepository userRepository, DoctorRepository doctorRepository, CareTrackingRepository careTrackingRepository, DocumentRepository documentRepository, FileStorageService fileStorageService) {
        this.getCurrentUserContext = getCurrentUserContext;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.careTrackingRepository = careTrackingRepository;
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
    }

    public DeleteCareTrackingDocumentResponse execute(UUID carTrackingId, UUID documentId) {
        try {
            String username = this.getCurrentUserContext.getUsername();
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(carTrackingId, doctor);
            CareTrackingDocument document = careTracking.getById(documentId);

            if (!Objects.equals(doctor.getId(), document.getDocument().getUploadedBy())) {
                throw new CannotDeleteDocument();
            }

            this.documentRepository.delete(document.getDocument());
            this.fileStorageService.delete(document.getDocument().getPath());

            return new DeleteCareTrackingDocumentResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
