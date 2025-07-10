package fr.esgi.doctodocapi.use_cases.doctor.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.GetDoctorNotificationResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IGetDoctorNotifications;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GetDoctorNotifications implements IGetDoctorNotifications {
    private final NotificationRepository notificationRepository;
    private final GetDoctorFromContext getDoctorFromContext;
    private final NotificationDoctorPresentationMapper notificationDoctorPresentationMapper;

    public GetDoctorNotifications(NotificationRepository notificationRepository, GetDoctorFromContext getDoctorFromContext, NotificationDoctorPresentationMapper notificationDoctorPresentationMapper) {
        this.notificationRepository = notificationRepository;
        this.getDoctorFromContext = getDoctorFromContext;
        this.notificationDoctorPresentationMapper = notificationDoctorPresentationMapper;
    }

    public List<GetDoctorNotificationResponse> getAll() {
        try {
            Doctor doctor = this.getDoctorFromContext.get();
            List<Notification> notifications = this.notificationRepository.getAllByRecipientId(doctor.getId());
            return notifications.stream().map(this.notificationDoctorPresentationMapper::toDto).toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
