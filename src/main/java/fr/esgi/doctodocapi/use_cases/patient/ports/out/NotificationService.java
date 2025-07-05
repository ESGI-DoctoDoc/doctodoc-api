package fr.esgi.doctodocapi.use_cases.patient.ports.out;

public interface NotificationService {
    void send(String fcmToken, NotificationMessage message);
}
