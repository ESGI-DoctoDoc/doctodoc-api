package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotJpaRepository extends JpaRepository<SlotEntity, UUID> {
    List<SlotEntity> findAllByMedicalConcerns_IdAndDate(UUID medicalConcernId, LocalDate date);
    List<SlotEntity> findAllByDoctor_IdAndDateAfter(UUID doctorId, LocalDate date);
    Page<SlotEntity> findAllByDoctor_IdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Optional<SlotEntity> findFirstByMedicalConcerns_IdAndDate(UUID medicalConcernId, LocalDate date);
    Page<SlotEntity> findAllByDoctor_IdAndDateGreaterThanEqual(UUID doctorId, LocalDate date, Pageable pageable);
}
