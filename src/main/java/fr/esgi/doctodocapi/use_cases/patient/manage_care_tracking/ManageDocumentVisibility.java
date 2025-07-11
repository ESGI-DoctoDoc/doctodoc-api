package fr.esgi.doctodocapi.use_cases.patient.manage_care_tracking;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.care_tracking.CareTracking;
import fr.esgi.doctodocapi.model.care_tracking.CareTrackingRepository;
import fr.esgi.doctodocapi.model.care_tracking.documents.CareTrackingDocument;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.notification.NotificationsType;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_care_tracking.IManageDocumentVisibility;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManageDocumentVisibility implements IManageDocumentVisibility {
    private final CareTrackingRepository careTrackingRepository;
    private final GetPatientFromContext getPatientFromContext;
    private final NotificationRepository notificationRepository;

    public ManageDocumentVisibility(CareTrackingRepository careTrackingRepository, GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository) {
        this.careTrackingRepository = careTrackingRepository;
        this.getPatientFromContext = getPatientFromContext;
        this.notificationRepository = notificationRepository;
    }

    public void share(UUID careTrackingId, UUID documentId) {
        try {
            Patient patient = this.getPatientFromContext.get();
            CareTracking careTracking = this.careTrackingRepository.getByIdAndPatient(careTrackingId, patient);
            CareTrackingDocument document = careTracking.getById(documentId);
            document.shared(patient.getId());
            this.careTrackingRepository.save(careTracking);

            if (document.isShared()) {
                notifyDoctorOfNewDocuments(careTracking);
            }
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }

    private void notifyDoctorOfNewDocuments(CareTracking careTracking) {
        List<UUID> doctors = new ArrayList<>(careTracking.getDoctors());
        doctors.add(careTracking.getCreatorId());

        doctors.forEach(doctorId -> {
                    Notification notification = NotificationsType.newDocumentsInCareTracking(doctorId, careTracking.getCaseName());
                    this.notificationRepository.save(notification);
                }
        );
    }
}
