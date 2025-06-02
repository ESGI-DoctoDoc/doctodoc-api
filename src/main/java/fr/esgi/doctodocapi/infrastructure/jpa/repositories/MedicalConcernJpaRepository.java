package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalConcernEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalConcernJpaRepository extends JpaRepository<MedicalConcernEntity, UUID> {
    List<MedicalConcernEntity> findAllByDoctor_Id(UUID id);
}
