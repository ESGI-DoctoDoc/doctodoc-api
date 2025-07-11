package fr.esgi.doctodocapi.use_cases.admin.notifications;

import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.GetAdminNotificationResponse;
import org.springframework.stereotype.Service;

@Service
public class NotificationAdminPresentationMapper {
    public GetAdminNotificationResponse toDto(Notification notification) {
        return new GetAdminNotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getSendAt(),
                notification.isRead()
        );
    }
}
