package fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push;

public interface NotificationPushService {
    void send(NotificationMessage message);
}
