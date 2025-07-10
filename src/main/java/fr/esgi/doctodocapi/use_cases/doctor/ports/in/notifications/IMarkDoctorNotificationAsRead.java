package fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.MarkAsReadDoctorNotificationResponse;

import java.util.UUID;

public interface IMarkDoctorNotificationAsRead {
    MarkAsReadDoctorNotificationResponse process(UUID id);
}
