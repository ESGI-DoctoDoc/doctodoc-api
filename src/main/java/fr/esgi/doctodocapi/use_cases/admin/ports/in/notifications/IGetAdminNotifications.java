package fr.esgi.doctodocapi.use_cases.admin.ports.in.notifications;

import fr.esgi.doctodocapi.use_cases.admin.dtos.responses.notification_response.GetAdminNotificationResponse;

import java.util.List;

public interface IGetAdminNotifications {
    List<GetAdminNotificationResponse> getAll();
}
