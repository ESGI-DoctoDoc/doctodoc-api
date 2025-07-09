package fr.esgi.doctodocapi.configuration.patient;

import fr.esgi.doctodocapi.infrastructure.security.service.GetPatientFromContext;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.use_cases.patient.notifications.GetNotifications;
import fr.esgi.doctodocapi.use_cases.patient.notifications.MarkReadOnNotification;
import fr.esgi.doctodocapi.use_cases.patient.notifications.NotificationPresentationMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IGetNotifications;
import fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications.IMarkReadOnNotification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationPatientConfiguration {
    @Bean
    public IGetNotifications getNotifications(GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository, NotificationPresentationMapper notificationMapper) {
        return new GetNotifications(getPatientFromContext, notificationRepository, notificationMapper);
    }

    @Bean
    public IMarkReadOnNotification markReadOnNotification(GetPatientFromContext getPatientFromContext, NotificationRepository notificationRepository) {
        return new MarkReadOnNotification(getPatientFromContext, notificationRepository);
    }
}
