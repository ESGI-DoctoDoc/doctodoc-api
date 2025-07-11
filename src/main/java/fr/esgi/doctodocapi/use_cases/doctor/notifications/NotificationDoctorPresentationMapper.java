package fr.esgi.doctodocapi.use_cases.doctor.notifications;

import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.GetDoctorNotificationResponse;
import org.springframework.stereotype.Service;

@Service
public class NotificationDoctorPresentationMapper {
    public GetDoctorNotificationResponse toDto(Notification notification) {
        return new GetDoctorNotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getSendAt(),
                notification.isRead()
        );
    }
}
