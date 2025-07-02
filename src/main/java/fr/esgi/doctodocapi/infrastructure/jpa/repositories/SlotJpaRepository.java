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
    Optional<SlotEntity> findFirstByMedicalConcerns_IdAndDate(UUID medicalConcernId, LocalDate date);

    @Query(value = """
    SELECT DISTINCT s.*
    FROM slots s
    LEFT JOIN slots_medical_concerns smc ON s.slot_id = smc.slot_id
    LEFT JOIN medical_concerns mc ON smc.medical_concern_id = mc.medical_concern_id
    LEFT JOIN appointments a ON s.slot_id = a.slot_id
    WHERE s.doctor_id = :doctorId
    AND s.date >= :startDate
    AND (
        EXISTS (
            SELECT 1 FROM slots_medical_concerns smc1
            JOIN medical_concerns mc1 ON smc1.medical_concern_id = mc1.medical_concern_id 
            WHERE smc1.slot_id = s.slot_id AND mc1.deleted_at IS NULL
        )
        OR
        (NOT EXISTS (
            SELECT 1 FROM slots_medical_concerns smc2 
            JOIN medical_concerns mc2 ON smc2.medical_concern_id = mc2.medical_concern_id 
            WHERE smc2.slot_id = s.slot_id AND mc2.deleted_at IS NULL
        )
        AND EXISTS (
            SELECT 1 FROM appointments a2 
            WHERE a2.slot_id = s.slot_id AND a2.status IN (:validStatuses)
        ))
    )
""", nativeQuery = true)
    Page<SlotEntity> findVisibleByDoctorIdAndDateAfter(
            @Param("doctorId") UUID doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("validStatuses") List<String> validStatuses,
            Pageable pageable
    );

    @Query(value = """
    SELECT DISTINCT s.*
    FROM slots s
    LEFT JOIN slots_medical_concerns smc ON s.slot_id = smc.slot_id
    LEFT JOIN medical_concerns mc ON smc.medical_concern_id = mc.medical_concern_id
    LEFT JOIN appointments a ON s.slot_id = a.slot_id
    WHERE s.doctor_id = :doctorId
    AND s.date BETWEEN :startDate AND :endDate
    AND (
        EXISTS (
            SELECT 1 FROM slots_medical_concerns smc1
            JOIN medical_concerns mc1 ON smc1.medical_concern_id = mc1.medical_concern_id 
            WHERE smc1.slot_id = s.slot_id AND mc1.deleted_at IS NULL
        )
        OR
        (NOT EXISTS (
            SELECT 1 FROM slots_medical_concerns smc2 
            JOIN medical_concerns mc2 ON smc2.medical_concern_id = mc2.medical_concern_id 
            WHERE smc2.slot_id = s.slot_id AND mc2.deleted_at IS NULL
        )
        AND EXISTS (
            SELECT 1 FROM appointments a2 
            WHERE a2.slot_id = s.slot_id AND a2.status IN (:validStatuses)
        ))
    )
""", nativeQuery = true)
    Page<SlotEntity> findVisibleByDoctorIdAndDateBetween(
            @Param("doctorId") UUID doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("validStatuses") List<String> validStatuses,
            Pageable pageable
    );
}
