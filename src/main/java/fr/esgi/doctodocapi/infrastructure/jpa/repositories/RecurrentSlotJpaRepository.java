package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.RecurrentSlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecurrentSlotJpaRepository extends JpaRepository<RecurrentSlotEntity, UUID> {
}

