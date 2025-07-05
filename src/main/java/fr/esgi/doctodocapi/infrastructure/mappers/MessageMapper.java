package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.MessageEntity;
import fr.esgi.doctodocapi.model.doctor.care_tracking.message.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    public Message toDomain(MessageEntity entity) {
        return new Message(
                entity.getId(),
                entity.getSender().getId(),
                entity.getCareTracking().getId(),
                entity.getContent(),
                entity.getSentAt()
        );
    }

    public MessageEntity toEntity(Message domain, DoctorEntity sender, CareTrackingEntity careTracking) {
        MessageEntity entity = new MessageEntity();
        entity.setSender(sender);
        entity.setCareTracking(careTracking);
        entity.setContent(domain.getContent());
        entity.setSentAt(domain.getSentAt());
        return entity;
    }
}
