package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CareTrackingJpaRepository extends JpaRepository<CareTrackingEntity, UUID> {
    // doctor
    Page<CareTrackingEntity> findAllByCreator_Id(UUID doctorId, Pageable pageable);
    Optional<CareTrackingEntity> findByIdAndPatient_Id(UUID careTrackingId, UUID patientId);
    Optional<CareTrackingEntity> findByIdAndPatient_IdAndCreator_Id(UUID careTrackingId, UUID patientId, UUID doctorId);

    // patient
    Page<CareTrackingEntity> findAllByPatient_IdOrderByCreatedAtDesc(UUID patientId, Pageable pageable);

}
