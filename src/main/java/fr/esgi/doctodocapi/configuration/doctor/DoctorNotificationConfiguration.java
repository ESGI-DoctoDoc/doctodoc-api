package fr.esgi.doctodocapi.configuration.doctor;

import fr.esgi.doctodocapi.infrastructure.security.service.GetDoctorFromContext;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import fr.esgi.doctodocapi.use_cases.doctor.notifications.GetDoctorNotifications;
import fr.esgi.doctodocapi.use_cases.doctor.notifications.MarkDoctorNotificationAsRead;
import fr.esgi.doctodocapi.use_cases.doctor.notifications.NotificationDoctorPresentationMapper;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IGetDoctorNotifications;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications.IMarkDoctorNotificationAsRead;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DoctorNotificationConfiguration {

    @Bean
    public IGetDoctorNotifications getDoctorNotifications(NotificationRepository notificationRepository, GetDoctorFromContext getDoctorFromContext, NotificationDoctorPresentationMapper notificationDoctorPresentationMapper) {
        return new GetDoctorNotifications(notificationRepository, getDoctorFromContext, notificationDoctorPresentationMapper);

    }

    @Bean
    public IMarkDoctorNotificationAsRead markDoctorNotificationAsRead(GetDoctorFromContext getDoctorFromContext, NotificationRepository notificationRepository) {
        return new MarkDoctorNotificationAsRead(getDoctorFromContext, notificationRepository);

    }
}
