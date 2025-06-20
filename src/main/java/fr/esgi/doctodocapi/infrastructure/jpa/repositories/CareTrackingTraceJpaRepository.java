package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CareTrackingTraceJpaRepository extends JpaRepository<CareTrackingEntity, UUID> {
}
