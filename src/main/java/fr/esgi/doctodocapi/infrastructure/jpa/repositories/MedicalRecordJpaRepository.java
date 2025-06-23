package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecordEntity, UUID> {
    Optional<MedicalRecordEntity> findByPatientId(UUID patientId);
}
