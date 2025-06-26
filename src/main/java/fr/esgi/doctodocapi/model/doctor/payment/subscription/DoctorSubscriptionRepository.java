package fr.esgi.doctodocapi.model.doctor.payment.subscription;

import java.util.Optional;
import java.util.UUID;

public interface DoctorSubscriptionRepository {
    DoctorSubscription save(DoctorSubscription subscription);
    Optional<DoctorSubscription> findLatestSubscriptionByDoctorId(UUID doctorId);
    Optional<DoctorSubscription> findActivePaidSubscriptionByDoctorId(UUID doctorId);
}
