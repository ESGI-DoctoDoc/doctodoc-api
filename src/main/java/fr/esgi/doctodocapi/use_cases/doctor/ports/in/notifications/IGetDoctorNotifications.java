package fr.esgi.doctodocapi.use_cases.doctor.ports.in.notifications;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.notification_response.GetDoctorNotificationResponse;

import java.util.List;

public interface IGetDoctorNotifications {
    List<GetDoctorNotificationResponse> getAll();
}
