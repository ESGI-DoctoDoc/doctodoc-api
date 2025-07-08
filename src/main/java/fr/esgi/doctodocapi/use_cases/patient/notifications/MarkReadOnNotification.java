package fr.esgi.doctodocapi.use_cases.patient.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IMarkReadOnNotification;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class MarkReadOnNotification implements IMarkReadOnNotification {
    private final GetPatientFromContext getPatientFromContext;
    private final NotificationRepository notificationRepository;

    public MarkReadOnNotification(GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository) {
        this.getPatientFromContext = getPatientFromContext;
        this.notificationRepository = notificationRepository;
    }

    public void process(UUID id) {
        try {
            Patient patient = this.getPatientFromContext.get();
            Notification notification = this.notificationRepository.getByIdAndRecipientId(id, patient.getUserId());
            notification.setRead(true);
            this.notificationRepository.markAsRead(notification);
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
