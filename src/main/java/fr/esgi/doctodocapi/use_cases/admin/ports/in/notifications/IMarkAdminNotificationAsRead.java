package fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.MarkAsReadAdminNotificationResponse;

import java.util.UUID;

public interface IMarkAdminNotificationAsRead {
    MarkAsReadAdminNotificationResponse process(UUID id);
}
