package fr.esgi.doctodocapi.infrastructure.services.care_tracking;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MessageEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MessageJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.MessageMapper;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepositoryImpl.class);
    private static final int PAGE_SIZE = 8;

    private final MessageJpaRepository messageJpaRepository;
    private final EntityManager entityManager;
    private final MessageMapper messageMapper;

    public MessageRepositoryImpl(MessageJpaRepository messageJpaRepository, EntityManager entityManager, MessageMapper messageMapper) {
        this.messageJpaRepository = messageJpaRepository;
        this.entityManager = entityManager;
        this.messageMapper = messageMapper;
    }

    @Override
    public void save(Message message) {
        logger.info("[MessageRepositoryImpl] Sauvegarde du message pour careTrackingId: {}, senderId: {}",
                message.getCareTrackingId(), message.getSenderId());
        DoctorEntity doctor = this.entityManager.getReference(DoctorEntity.class, message.getSenderId());
        CareTrackingEntity careTracking = this.entityManager.getReference(CareTrackingEntity.class, message.getCareTrackingId());
        MessageEntity messageEntity = this.messageMapper.toEntity(message, doctor, careTracking);
        this.messageJpaRepository.save(messageEntity);
        logger.info("[MessageRepositoryImpl] Message sauvegardé: {}", messageEntity.getId());
    }

    @Override
    public List<Message> findByCareTrackingId(UUID careTrackingId) {
        logger.info("[MessageRepositoryImpl] Recherche de tous les messages pour careTrackingId: {}", careTrackingId);
        List<MessageEntity> messageEntities = this.messageJpaRepository.findByCareTrackingId(careTrackingId);
        List<Message> messages = messageEntities.stream()
                .map(messageMapper::toDomain)
                .toList();
        logger.info("[MessageRepositoryImpl] Messages trouvés (count: {}): {}", messages.size(), messages);
        return messages;
    }

    @Override
    public List<Message> findLatestMessages(UUID careTrackingId) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<MessageEntity> entities = messageJpaRepository.findLatestMessages(careTrackingId, pageable);
        return entities.stream()
                .map(messageMapper::toDomain)
                .toList();
    }

    @Override
    public List<Message> findPreviousMessages(UUID careTrackingId, LocalDateTime sentAt, UUID id) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        List<MessageEntity> entities = messageJpaRepository.findPreviousMessages(careTrackingId, sentAt, id, pageable);
        return entities.stream().map(messageMapper::toDomain).toList();
    }
}