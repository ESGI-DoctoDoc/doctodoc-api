package fr.esgi.doctodocapi.configuration.admin;

import fr.esgi.doctodocapi.infrastructure.security.service.GetUserFromContext;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.use_cases.admin.notifications.GetAdminNotifications;
import fr.esgi.doctodocapi.use_cases.admin.notifications.MarkAdminNotificationAsRead;
import fr.esgi.doctodocapi.use_cases.admin.notifications.NotificationAdminPresentationMapper;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IGetAdminNotifications;
import fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications.IMarkAdminNotificationAsRead;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminNotificationConfiguration {

    @Bean
    public IGetAdminNotifications getAdminNotifications(NotificationRepository notificationRepository, GetUserFromContext getCurrentUserContext, NotificationAdminPresentationMapper notificationAdminPresentationMapper) {
        return new GetAdminNotifications(notificationRepository, getCurrentUserContext, notificationAdminPresentationMapper);

    }

    @Bean
    public IMarkAdminNotificationAsRead markAdminNotificationAsRead(NotificationRepository notificationRepository, GetUserFromContext getUserFromContext) {
        return new MarkAdminNotificationAsRead(notificationRepository, getUserFromContext);

    }
}
