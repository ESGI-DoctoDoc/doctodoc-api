package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorSubscriptionJpaRepository extends JpaRepository<DoctorSubscriptionEntity, UUID> {
    @Query(value = """
            SELECT s.*
            FROM doctor_subscriptions s
            JOIN doctor_invoices i ON i.subscription_id = s.id
            WHERE s.doctor_id = :doctorId
            AND s.end_date::date >= CAST(:now AS date)
            AND i.state = 'PAID'
            ORDER BY s.end_date DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<DoctorSubscriptionEntity> findFirstPaidSubscriptionByDoctor(UUID doctorId, LocalDate now);


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
