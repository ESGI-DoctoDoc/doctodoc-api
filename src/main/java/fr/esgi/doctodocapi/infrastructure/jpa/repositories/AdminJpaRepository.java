package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminJpaRepository extends JpaRepository<AdminEntity, UUID> {
    boolean existsByUser_id(UUID userId);
}
