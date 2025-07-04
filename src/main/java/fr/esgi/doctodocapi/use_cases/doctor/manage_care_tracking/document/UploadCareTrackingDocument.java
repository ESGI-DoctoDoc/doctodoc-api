package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.ClosedCareTrackingException;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetUrlUploadForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IUploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UploadCareTrackingDocument implements IUploadCareTrackingDocument {

    private final CareTrackingRepository careTrackingRepository;
    private final FileStorageService uploadFile;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorRepository doctorRepository;
    private final DocumentRepository documentRepository;

    public UploadCareTrackingDocument(CareTrackingRepository careTrackingRepository, FileStorageService uploadFile, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository, DocumentRepository documentRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.uploadFile = uploadFile;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorRepository = doctorRepository;
        this.documentRepository = documentRepository;
    }

    public GetDocumentForCareTrackingResponse execute(UUID careTrackingId, SaveDocumentRequest request) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = retrieveAndValidateCareTracking(careTrackingId, doctor);
            UUID patientId = careTracking.getPatient().getId();

            CareTrackingDocument careTrackingDocument = CareTrackingDocument.create(
                    request.filename(),
                    request.type(),
                    patientId,
                    user.getId(),
                    careTracking.getId()
            );

            careTracking.addDocument(careTrackingDocument);
            this.careTrackingRepository.save(careTracking);

            return new GetDocumentForCareTrackingResponse(
                    careTrackingDocument.getDocument().getId(),
                    careTrackingDocument.getDocument().getName(),
                    careTrackingDocument.getDocument().getType().getValue(),
                    careTrackingDocument.getDocument().getPath()
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }

    public GetUrlUploadForCareTrackingResponse getPresignedUrlToUpload(UUID id) {
        try {
            Document document = this.documentRepository.getById(id);
            String url = this.uploadFile.getPresignedUrlToUpload(document.getPath());
            return new GetUrlUploadForCareTrackingResponse(url);
        } catch (DocumentNotFoundException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }


    private CareTracking retrieveAndValidateCareTracking(UUID careTrackingId, Doctor doctor) {
        CareTracking careTracking = this.careTrackingRepository.getByIdAndDoctor(careTrackingId, doctor);

        if (careTracking.getClosedAt() != null) {
            throw new ClosedCareTrackingException();
        }

        return careTracking;
    }
}
