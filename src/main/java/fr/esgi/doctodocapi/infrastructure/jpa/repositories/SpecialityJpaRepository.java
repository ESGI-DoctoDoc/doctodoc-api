package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpecialityJpaRepository extends JpaRepository<SpecialityEntity, UUID> {
    Optional<SpecialityEntity> findByName(String name);
}
