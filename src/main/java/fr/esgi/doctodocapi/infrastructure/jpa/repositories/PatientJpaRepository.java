package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
    boolean existsByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    Optional<PatientEntity> findByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    List<PatientEntity> findAllByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    Page<PatientEntity> findByDoctor_IdAndDeletedAtIsNull(UUID doctorId, Pageable pageable);
}
