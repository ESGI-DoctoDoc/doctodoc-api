package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface DoctorSubscriptionJpaRepository extends JpaRepository<DoctorSubscriptionEntity, UUID> {
    Optional<DoctorSubscriptionEntity> findFirstByDoctorAndEndDateAfterOrderByEndDateDesc(DoctorEntity doctor, LocalDateTime now);
}
