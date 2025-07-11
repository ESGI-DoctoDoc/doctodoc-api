package fr.esgi.doctodocapi.use_cases.admin.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetUserFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.MarkAsReadAdminNotificationResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IMarkAdminNotificationAsRead;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class MarkAdminNotificationAsRead implements IMarkAdminNotificationAsRead {
    private final NotificationRepository notificationRepository;
    private final GetUserFromContext getUserFromContext;

    public MarkAdminNotificationAsRead(NotificationRepository notificationRepository, GetUserFromContext getUserFromContext) {
        this.notificationRepository = notificationRepository;
        this.getUserFromContext = getUserFromContext;
    }

    public MarkAsReadAdminNotificationResponse process(UUID id) {
        try {
            User user = this.getUserFromContext.get();
            Notification notification = this.notificationRepository.getByIdAndRecipientId(id, user.getId());
            notification.setRead(true);
            this.notificationRepository.markAsRead(notification);
            return new MarkAsReadAdminNotificationResponse();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getCode(), e.getMessage());
        }
    }
}
