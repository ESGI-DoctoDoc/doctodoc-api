package fr.esgi.doctodocapi.use_cases.admin.notifications;

import fr.esgi.doctodocapi.infrastructure.security.service.GetUserFromContext;
import fr.esgi.doctodocapi.model.DomainException;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.GetAdminNotificationResponse;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IGetAdminNotifications;
import fr.esgi.doctodocapi.use_cases.exceptions.ApiException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class GetAdminNotifications implements IGetAdminNotifications {
    private final NotificationRepository notificationRepository;
    private final GetUserFromContext getCurrentUserContext;
    private final NotificationAdminPresentationMapper notificationAdminPresentationMapper;

    public GetAdminNotifications(NotificationRepository notificationRepository, GetUserFromContext getCurrentUserContext, NotificationAdminPresentationMapper notificationAdminPresentationMapper) {
        this.notificationRepository = notificationRepository;
        this.getCurrentUserContext = getCurrentUserContext;
        this.notificationAdminPresentationMapper = notificationAdminPresentationMapper;
    }

    public List<GetAdminNotificationResponse> getAll() {
        try {
            User user = this.getCurrentUserContext.get();
            List<Notification> notifications = this.notificationRepository.getAllByRecipientId(user.getId());

            LocalDateTime nowMinusOneWeek = LocalDateTime.now().minusWeeks(1);

            List<Notification> notificationsSentInLastWeek = notifications
                    .stream()
                    .filter(notification -> notification.getSendAt().isEqual(nowMinusOneWeek) || notification.getSendAt().isAfter(nowMinusOneWeek))
                    .sorted(Comparator.comparing(Notification::getSendAt).reversed())
                    .toList();

            return notificationsSentInLastWeek.stream().map(this.notificationAdminPresentationMapper::toDto).toList();
        } catch (DomainException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, e.getCode(), e.getMessage());
        }

    }
}
