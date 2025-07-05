package fr.esgi.doctodocapi.infrastructure.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseMessagingServiceImpl.class);

    private static Message build(String fcmToken, NotificationMessage notificationMessage) {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .build();

        if (notificationMessage.getDatas() == null) {
            return Message.builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .build();
        } else {
            return Message.builder()
                    .setToken(fcmToken)
                    .setNotification(notification)
                    .putAllData(notificationMessage.getDatas())
                    .build();
        }
    }

    @Override
    public void send(String fcmToken, NotificationMessage message) {
        Message firebaseMessageBuild = build(fcmToken, message);

        try {
            String response = FirebaseMessaging.getInstance().send(firebaseMessageBuild);
            logger.info("Successfully sent message: {}", response);

        } catch (FirebaseMessagingException e) {
            logger.error("Cannot send notification.");
        }

    }
}
