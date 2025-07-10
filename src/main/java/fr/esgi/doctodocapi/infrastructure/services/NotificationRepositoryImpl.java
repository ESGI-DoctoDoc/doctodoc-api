package fr.esgi.doctodocapi.infrastructure.services;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.NotificationEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.NotificationJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.NotificationMapper;
import fr.esgi.doctodocapi.model.notification.Notification;
import fr.esgi.doctodocapi.model.notification.NotificationNotFoundException;
import fr.esgi.doctodocapi.model.notification.NotificationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;

    public NotificationRepositoryImpl(NotificationJpaRepository notificationJpaRepository, NotificationMapper notificationMapper) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public List<Notification> getNotificationsNotReadByRecipientId(UUID recipientId) {
        List<NotificationEntity> notifications = this.notificationJpaRepository.findAllByRecipientIdAndIsReadIsFalse(recipientId);
        return notifications.stream().map(this.notificationMapper::toDomain).toList();
    }

    @Override
    public Notification getByIdAndRecipientId(UUID id, UUID userId) throws NotificationNotFoundException {
        NotificationEntity entity = this.notificationJpaRepository.findByIdAndRecipientId(id, userId).orElseThrow(NotificationNotFoundException::new);
        return this.notificationMapper.toDomain(entity);
    }

    @Override
    public void markAsRead(Notification notification) {
        NotificationEntity entity = this.notificationJpaRepository.findById(notification.getId()).orElseThrow(NotificationNotFoundException::new);
        entity.setIsRead(notification.isRead());
        this.notificationJpaRepository.save(entity);

    }

    @Override
    public List<Notification> getAllByRecipientId(UUID id) {
        List<NotificationEntity> notifications = this.notificationJpaRepository.findAllByRecipientId(id);
        return notifications.stream().map(notificationMapper::toDomain).toList();
    }
}
