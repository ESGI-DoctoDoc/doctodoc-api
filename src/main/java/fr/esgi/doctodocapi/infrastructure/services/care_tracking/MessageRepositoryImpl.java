package fr.esgi.doctodocapi.infrastructure.services.care_tracking;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MessageEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.MessageJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.MessageMapper;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.MessageRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
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
        DoctorEntity doctor = this.entityManager.getReference(DoctorEntity.class, message.getSenderId());
        CareTrackingEntity careTracking = this.entityManager.getReference(CareTrackingEntity.class, message.getCareTrackingId());
        MessageEntity messageEntity = this.messageMapper.toEntity(message, doctor, careTracking);
        this.messageJpaRepository.save(messageEntity);
    }

    @Override
    public List<Message> findByCareTrackingIdAndDoctorId(UUID careTrackingId, UUID doctorId) {
        List<MessageEntity> messageEntities = this.messageJpaRepository.findByCareTrackingIdAndSenderId(careTrackingId, doctorId);
        return messageEntities.stream()
                .map(messageMapper::toDomain)
                .toList();
    }
}
