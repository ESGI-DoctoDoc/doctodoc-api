package fr.esgi.doctodocapi.use_cases.patient.notifications;

import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetNotificationResponse;
import org.springframework.stereotype.Service;

@Service
public class NotificationPresentationMapper {
    public GetNotificationResponse toDto(Notification notification) {
        return new GetNotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getSendAt(),
                notification.isRead()
        );
    }
}
