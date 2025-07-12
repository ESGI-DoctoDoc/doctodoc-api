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
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.user.MailSender;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.SaveDocumentRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetDocumentForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.document.GetUrlUploadForCareTrackingResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_document.IUploadCareTrackingDocument;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.FileStorageService;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessageType;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UploadCareTrackingDocument implements IUploadCareTrackingDocument {

    private final CareTrackingRepository careTrackingRepository;
    private final FileStorageService uploadFile;
    private final UserRepository userRepository;
    private final GetCurrentUserContext getCurrentUserContext;
    private final DoctorRepository doctorRepository;
    private final DocumentRepository documentRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationPushService notificationPushService;
    private final MailSender mailSender;

    public UploadCareTrackingDocument(CareTrackingRepository careTrackingRepository, FileStorageService uploadFile, UserRepository userRepository, GetCurrentUserContext getCurrentUserContext, DoctorRepository doctorRepository, DocumentRepository documentRepository, NotificationRepository notificationRepository, NotificationPushService notificationPushService, MailSender mailSender) {
        this.careTrackingRepository = careTrackingRepository;
        this.uploadFile = uploadFile;
        this.userRepository = userRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.doctorRepository = doctorRepository;
        this.documentRepository = documentRepository;
        this.notificationRepository = notificationRepository;
        this.notificationPushService = notificationPushService;
        this.mailSender = mailSender;
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
                    careTracking.getId(),
                    true
            );

            careTracking.addDocument(careTrackingDocument);
            this.careTrackingRepository.save(careTracking);

            notifyDoctorOfNewDocuments(careTracking, doctor.getId());
            notifyPatientOfNewDocuments(careTracking, doctor);
            sendMail(careTracking, doctor);

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

    /// Gestion des notifications et mail (à déplacer)

    private void sendMail(CareTracking careTracking, Doctor doctor) {
        String patientFirstName = careTracking.getPatient().getFirstName();
        String doctorFirstName = doctor.getPersonalInformations().getFirstName();
        String doctorLastName = doctor.getPersonalInformations().getLastName();
        String careTrackingName = careTracking.getCaseName();

        String subject = "Nouveaux documents dans le suivi de dossier " + careTrackingName;

        String body = String.format("""
                        Bonjour %s,
                        
                        Le Dr %s %s a mis à disposition de nouveaux documents dans votre suivi de dossier %s.
                        
                        Cordialement,
                        L’équipe Doctodoc.
                        """,
                patientFirstName,
                doctorFirstName,
                doctorLastName,
                careTrackingName
        );

        this.mailSender.sendMail(
                careTracking.getPatient().getEmail().getValue(),
                subject,
                body
        );
    }

    private void notifyDoctorOfNewDocuments(CareTracking careTracking, UUID uploaderId) {
        List<UUID> doctors = new ArrayList<>(careTracking.getDoctors());
        doctors.add(careTracking.getCreatorId());

        doctors = doctors.stream().filter(id -> !Objects.equals(uploaderId, id)).toList();

        doctors.forEach(doctorId -> {
                    Notification notification = NotificationsType.newDocumentsInCareTracking(doctorId, careTracking.getCaseName());
                    this.notificationRepository.save(notification);
                }
        );
    }

    private void notifyPatientOfNewDocuments(CareTracking careTracking, Doctor doctor) {
        String doctorFullName = doctor.getPersonalInformations().getFirstName() + " " + doctor.getPersonalInformations().getLastName();
        NotificationMessage message = NotificationMessageType.newDocumentsInCareTracking(
                careTracking.getPatient().getUserId(),
                careTracking.getId(),
                careTracking.getCaseName(),
                LocalDateTime.now(),
                doctorFullName
        );
        this.notificationPushService.send(message);
    }
}
