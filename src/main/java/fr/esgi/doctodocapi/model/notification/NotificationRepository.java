package fr.esgi.doctodocapi.model.notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {
    List<Notification> getNotificationsNotReadByRecipientId(UUID recipientId);

    Notification getByIdAndRecipientId(UUID id, UUID userId) throws NotificationNotFoundException;

    void markAsRead(Notification notification);
}
