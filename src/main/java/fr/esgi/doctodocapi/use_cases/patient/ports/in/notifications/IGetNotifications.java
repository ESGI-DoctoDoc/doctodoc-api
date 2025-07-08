package fr.esgi.doctodocapi.use_cases.patient.ports.in.notifications;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.GetNotificationResponse;

import java.util.List;

public interface IGetNotifications {
    List<GetNotificationResponse> getSentInLastWeek();
}
