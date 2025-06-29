package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.appointment.AppointmentRepository;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.ClosedCareTrackingException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.document.DocumentNotFoundException;
import fr.esgi.doctodocapi.model.document.DocumentRepository;
import fr.esgi.doctodocapi.model.document.DocumentType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.document.GetUrlUploadForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IUploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document.CareTrackingFolders.FOLDER_CARE_TRACKING_FILE;
import static fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document.CareTrackingFolders.FOLDER_ROOT;

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

            String prefixPath = FOLDER_ROOT + patientId + FOLDER_CARE_TRACKING_FILE;

            Document document = Document.init(
                    request.filename(),
                    prefixPath,
                    DocumentType.fromValue(request.type()),
                    user.getId()
            );

            document.setPath(prefixPath + document.getId());

            careTracking.addDocument(document);

            this.careTrackingRepository.save(careTracking);

            return new GetDocumentForCareTrackingResponse(
                    document.getId(),
                    document.getName(),
                    document.getType().getValue(),
                    document.getPath()
            );

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
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
        CareTracking careTracking = this.careTrackingRepository.getById(careTrackingId);

        careTracking = this.careTrackingRepository.getByIdAndPatientIdAndDoctorId(
                careTrackingId,
                careTracking.getPatient(),
                doctor
        );

        if (careTracking.getClosedAt() != null) {
            throw new ClosedCareTrackingException();
        }

        return careTracking;
    }
}
