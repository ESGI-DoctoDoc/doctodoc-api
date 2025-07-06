package fr.esgi.doctodocapi.model.doctor.payment.subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorSubscriptionRepository {
    DoctorSubscription save(DoctorSubscription subscription);
    Optional<DoctorSubscription> findLatestSubscriptionByDoctorId(UUID doctorId);
    Optional<DoctorSubscription> findActivePaidSubscriptionByDoctorId(UUID doctorId);
    List<DoctorSubscription> findAllWithPagination(int page, int size);
    List<DoctorSubscription> findAllByDoctorId(UUID doctorId);
    List<DoctorSubscription> findAllByDoctorIdWithPagination(UUID doctorId, int page, int size);
}
