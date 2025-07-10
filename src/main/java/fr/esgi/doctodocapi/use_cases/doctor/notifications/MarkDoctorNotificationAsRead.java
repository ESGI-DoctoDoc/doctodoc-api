package fr.esgi.doctodocapi.use_cases.doctor.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.MarkAsReadDoctorNotificationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IMarkDoctorNotificationAsRead;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class MarkDoctorNotificationAsRead implements IMarkDoctorNotificationAsRead {
    private final GetDoctorFromContext getDoctorFromContext;
    private final NotificationRepository notificationRepository;

    public MarkDoctorNotificationAsRead(GetDoctorFromContext getDoctorFromContext, NotificationRepository notificationRepository) {
        this.getDoctorFromContext = getDoctorFromContext;
        this.notificationRepository = notificationRepository;
    }

    public MarkAsReadDoctorNotificationResponse process(UUID id) {
        try {
            Doctor doctor = this.getDoctorFromContext.get();
            Notification notification = this.notificationRepository.getByIdAndRecipientId(id, doctor.getId());
            notification.setRead(true);
            this.notificationRepository.markAsRead(notification);
            return new MarkAsReadDoctorNotificationResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
