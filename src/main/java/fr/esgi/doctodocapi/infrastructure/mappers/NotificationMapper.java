package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.NotificationEntity;
import fr.esgi.doctodocapi.use_cases.patient.ports.out.NotificationMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationMapper {
    public NotificationEntity toEntity(NotificationMessage message) {
        NotificationEntity entity = new NotificationEntity();
        entity.setRecipientId(message.getRecipientId());
        entity.setTitle(message.getTitle());
        entity.setContent(message.getBody());
        entity.setSendAt(LocalDateTime.now());

        return entity;
    }
}
