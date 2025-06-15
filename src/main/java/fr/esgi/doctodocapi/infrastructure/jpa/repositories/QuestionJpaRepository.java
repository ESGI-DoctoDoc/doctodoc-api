package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, UUID> {
    List<QuestionEntity> findAllByMedicalConcern_Id(UUID medicalConcernId);
}
