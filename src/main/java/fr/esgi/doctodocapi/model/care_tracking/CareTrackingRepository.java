package fr.esgi.doctodocapi.model.care_tracking;


import java.util.List;
import java.util.UUID;

public interface CareTrackingRepository {
    UUID save(CareTracking careTracking);
    List<CareTracking> findAll(UUID doctorId, int page, int size);
}
