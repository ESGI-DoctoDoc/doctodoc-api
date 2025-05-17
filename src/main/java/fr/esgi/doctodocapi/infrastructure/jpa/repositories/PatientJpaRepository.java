package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
    boolean existsByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    Optional<PatientEntity> findByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    List<PatientEntity> findAllByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);
}
