package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findByCareTrackingId(UUID careTrackingId);
}
