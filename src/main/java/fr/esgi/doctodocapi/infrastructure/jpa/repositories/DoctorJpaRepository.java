package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, UUID> {
    boolean existsByUser_Id(UUID userId);

    Optional<DoctorEntity> findById(UUID id);
}
