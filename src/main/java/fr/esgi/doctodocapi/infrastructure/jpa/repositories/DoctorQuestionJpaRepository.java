package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorQuestionJpaRepository extends JpaRepository<DoctorQuestionEntity, UUID> {
    List<DoctorQuestionEntity> findAllByMedicalConcern_Id(UUID id);
}
