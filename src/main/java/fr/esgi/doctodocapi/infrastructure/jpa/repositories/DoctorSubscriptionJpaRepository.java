package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorSubscriptionJpaRepository extends JpaRepository<DoctorSubscriptionEntity, UUID> {
    @Query("""
                SELECT s
                FROM DoctorSubscriptionEntity s
                JOIN DoctorInvoiceEntity i ON i.subscription = s
                WHERE s.doctor = :doctor
                AND s.endDate > :now
                AND i.state = 'PAID'
                ORDER BY s.endDate DESC
                LIMIT 1
            """)
    Optional<DoctorSubscriptionEntity> findFirstPaidSubscriptionByDoctor(DoctorEntity doctor, LocalDateTime now);


    @Query("""
    SELECT s
    FROM DoctorSubscriptionEntity s
    WHERE s.doctor = :doctor
    ORDER BY s.startDate DESC
    LIMIT 1
""")
    Optional<DoctorSubscriptionEntity> findLatestSubscriptionByDoctor(DoctorEntity doctor);
    List<DoctorSubscriptionEntity> findAllByDoctor(DoctorEntity doctor);
}
