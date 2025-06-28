package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorInvoiceJpaRepository extends JpaRepository<DoctorInvoiceEntity, UUID> {
    DoctorInvoiceEntity findBySessionId(String sessionId);
    Optional<DoctorInvoiceEntity> findBySubscription_Id(UUID subscriptionId);
}
