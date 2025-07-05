package fr.esgi.doctodocapi.model.doctor.care_tracking.message;


import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    List<Message> findByCareTrackingIdAndDoctorId(UUID careTrackingId, UUID doctorId);
    List<Message> findByCareTrackingId(UUID careTrackingId); // New method
}
