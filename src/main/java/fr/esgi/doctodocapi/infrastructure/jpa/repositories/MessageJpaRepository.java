package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageJpaRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findByCareTrackingId(UUID careTrackingId);

    @Query("""
    SELECT m FROM MessageEntity m
    WHERE m.careTracking.id = :careTrackingId
    ORDER BY m.sentAt DESC, m.id DESC
""")
    List<MessageEntity> findLatestMessages(
            @Param("careTrackingId") UUID careTrackingId,
            Pageable pageable
    );

    @Query("""
    SELECT m FROM MessageEntity m
    WHERE m.careTracking.id = :careTrackingId
      AND (
        m.sentAt < :sentAt OR
        (m.sentAt = :sentAt AND m.id < :id)
      )
    ORDER BY m.sentAt DESC, m.id DESC
    """)
    List<MessageEntity> findPreviousMessages(
            @Param("careTrackingId") UUID careTrackingId,
            @Param("sentAt") LocalDateTime sentAt,
            @Param("id") UUID id,
            Pageable pageable
    );
}