package fr.esgi.doctodocapi.use_cases.doctor.manage_care_tracking.document;

import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.doctor.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.doctor.care_tracking.ClosedCareTrackingException;
import fr.esgi.doctodocapi.model.document.Document;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IGetCareTrackingDocumentContent;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class GetCareTrackingDocumentContent implements IGetCareTrackingDocumentContent {
    private final CareTrackingRepository careTrackingRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final GetCurrentUserContext getCurrentUserContext;

    public GetCareTrackingDocumentContent(CareTrackingRepository careTrackingRepository, FileStorageService fileStorageService, UserRepository userRepository, DoctorRepository doctorRepository, GetCurrentUserContext getCurrentUserContext) {
        this.careTrackingRepository = careTrackingRepository;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.getCurrentUserContext = getCurrentUserContext;
    }

    public GetDocumentForCareTrackingResponse execute(UUID careTrackingId, UUID documentId) {
        String username = this.getCurrentUserContext.getUsername();

        try {
            User user = this.userRepository.findByEmail(username);
            Doctor doctor = this.doctorRepository.findDoctorByUserId(user.getId());

            CareTracking careTracking = retrieveAndValidateCareTracking(careTrackingId, doctor);

            Document document = careTracking.getById(documentId);

            String url = this.fileStorageService.getFile(document.getPath());

            return new GetDocumentForCareTrackingResponse(
                    document.getId(),
                    document.getName(),
                    document.getType().getValue(),
                    url
            );
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
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
