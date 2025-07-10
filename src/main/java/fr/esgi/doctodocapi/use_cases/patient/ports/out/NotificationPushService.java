package fr.esgi.doctodocapi.use_cases.patient.ports.out;

public interface NotificationPushService {
    void send(String fcmToken, NotificationMessage message);
}
