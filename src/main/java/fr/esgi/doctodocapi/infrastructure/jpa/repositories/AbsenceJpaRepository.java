package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AbsenceJpaRepository extends JpaRepository<AbsenceEntity, UUID> {
    List<AbsenceEntity> findAllByDoctor_Id(UUID doctorId);
}
