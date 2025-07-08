package fr.esgi.doctodocapi.use_cases.patient.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetNotificationResponse;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IGetNotifications;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class GetNotifications implements IGetNotifications {
    private final GetPatientFromContext getPatientFromContext;
    private final NotificationRepository notificationRepository;
    private final NotificationPresentationMapper notificationMapper;

    public GetNotifications(GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository, NotificationPresentationMapper notificationMapper) {
        this.getPatientFromContext = getPatientFromContext;
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public List<GetNotificationResponse> getSentInLastWeek() {
        try {
            Patient patient = this.getPatientFromContext.get();
            List<Notification> notifications = this.notificationRepository.getNotificationsNotReadByRecipientId(patient.getUserId());
            LocalDateTime nowMinusOneWeek = LocalDateTime.now().minusWeeks(1);

            List<Notification> notificationsSentInLastWeek = notifications
                    .stream()
                    .filter(notification -> notification.getSendAt().isEqual(nowMinusOneWeek) || notification.getSendAt().isAfter(nowMinusOneWeek))
                    .sorted(Comparator.comparing(Notification::getSendAt).reversed())
                    .toList();

            return notificationsSentInLastWeek.stream().map(this.notificationMapper::toDto).toList();

        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }
    }
}
