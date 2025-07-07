package fr.esgi.doctodocapi.model.doctor.care_tracking.message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);

    List<Message> findByCareTrackingId(UUID careTrackingId);

    List<Message> findLatestMessages(UUID careTrackingId);
    List<Message> findPreviousMessages(UUID careTrackingId, LocalDateTime sentAt, UUID id);
}
