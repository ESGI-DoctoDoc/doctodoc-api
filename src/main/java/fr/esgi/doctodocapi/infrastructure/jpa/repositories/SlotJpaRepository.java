package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotJpaRepository extends JpaRepository<SlotEntity, UUID> {
    @Query("""
    SELECT s FROM SlotEntity s
    JOIN s.medicalConcerns mc
    WHERE mc.id = :medicalConcernId
      AND mc.deletedAt IS NULL
      AND s.date = :date
""")
    List<SlotEntity> findAllByMedicalConcernIdAndDateAndMedicalConcernNotDeleted(
            @Param("medicalConcernId") UUID medicalConcernId,
            @Param("date") LocalDate date
    );

    List<SlotEntity> findAllByDoctor_IdAndDateAfter(UUID doctorId, LocalDate date);
    Page<SlotEntity> findAllByDoctor_IdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Optional<SlotEntity> findFirstByMedicalConcerns_IdAndDate(UUID medicalConcernId, LocalDate date);
    Page<SlotEntity> findAllByDoctor_IdAndDateGreaterThanEqual(UUID doctorId, LocalDate date, Pageable pageable);
}
