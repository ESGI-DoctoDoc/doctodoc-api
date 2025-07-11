package fr.esgi.doctodocapi.infrastructure.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.NotificationEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.NotificationJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.NotificationMapper;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.TokenFcmRepository;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationMessage;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.notification_push.NotificationPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingPushServiceImpl implements NotificationPushService {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseMessagingPushServiceImpl.class);

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;
    private final TokenFcmRepository tokenFcmRepository;

    public FirebaseMessagingPushServiceImpl(NotificationJpaRepository notificationJpaRepository, NotificationMapper notificationMapper, TokenFcmRepository tokenFcmRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.notificationMapper = notificationMapper;
        this.tokenFcmRepository = tokenFcmRepository;
    }

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
    public void send(NotificationMessage message) {
        String fcmToken = this.tokenFcmRepository.get(message.getRecipientId());
        if (fcmToken != null) {
            Message firebaseMessageBuild = build(fcmToken, message);

            try {
                String response = FirebaseMessaging.getInstance().send(firebaseMessageBuild);
                saveNotification(message);
                logger.info("Successfully sent message: {}", response);

            } catch (FirebaseMessagingException e) {
                logger.error("Cannot send notification.");
            }
        }
    }

    private void saveNotification(NotificationMessage message) {
        NotificationEntity notificationEntity = this.notificationMapper.toEntity(message);
        this.notificationJpaRepository.save(notificationEntity);
    }
}
