package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findAllBySlot_IdAndDeletedAtIsNull(UUID slotId);

    Page<AppointmentEntity> findAllByPatient_User_IdAndStatusOrderByDateDesc(UUID userId, String status, Pageable pageable);

    Page<AppointmentEntity> findAllByPatient_User_IdAndStatusOrderByDateAsc(UUID userId, String status, Pageable pageable);

    Optional<AppointmentEntity> findFirstByPatient_User_IdAndStatusAndDateIsGreaterThanEqualOrderByDateAsc(
            UUID userId, String status, LocalDate now);

    Page<AppointmentEntity> findAllByDoctor_Id(UUID doctorId, Pageable pageable);

    boolean existsByDoctor_IdAndPatient_Id(UUID doctorId, UUID patientId);

    @Query("""
    SELECT a
    FROM AppointmentEntity a
    JOIN a.medicalConcern mc
    WHERE a.doctor.id = :doctorId
    AND a.date BETWEEN :startDate AND :endDate
    AND a.status <> 'locked'
    AND (
        mc.deletedAt IS NULL
        OR (mc.deletedAt IS NOT NULL AND a.status IN :validStatuses)
    )
    ORDER BY a.date ASC
""")
    Page<AppointmentEntity> findVisibleAppointmentsByDoctorIdAndDateBetween(
            @Param("doctorId") UUID doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("validStatuses") List<String> validStatuses,
            Pageable pageable
    );

    @Query("""
    SELECT a
    FROM AppointmentEntity a
    JOIN a.medicalConcern mc
    WHERE a.doctor.id = :doctorId
    AND a.status <> 'locked'
    AND (
        mc.deletedAt IS NULL
        OR (mc.deletedAt IS NOT NULL AND a.status IN :validStatuses)
    )
    ORDER BY a.date ASC
""")
    Page<AppointmentEntity> findVisibleAppointmentsByDoctorId(
            @Param("doctorId") UUID doctorId,
            @Param("validStatuses") List<String> validStatuses,
            Pageable pageable
    );

    @Query("""
    SELECT a
    FROM AppointmentEntity a
    JOIN a.medicalConcern mc
    WHERE a.id = :appointmentId
    AND a.status <> 'locked'
    AND (
        mc.deletedAt IS NULL
        OR (mc.deletedAt IS NOT NULL AND a.status IN :validStatuses)
    )
""")
    Optional<AppointmentEntity> findVisibleById(
            @Param("appointmentId") UUID appointmentId,
            @Param("validStatuses") List<String> validStatuses
    );

    int countByDoctor_IdAndDateGreaterThanEqual(UUID doctorId, LocalDate date);

    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM AppointmentEntity a WHERE a.doctor.id = :doctorId")
    int countDistinctPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    Optional<AppointmentEntity> findByIdAndPatient_Id(UUID id, UUID patientId);

    @Query("""
    SELECT a
    FROM AppointmentEntity a
    WHERE a.status IN ('confirmed', 'completed' )
""")
    Page<AppointmentEntity> findAllVisibleForAdmin(Pageable pageable);

    void deleteAllByStatusAndLockedAtBeforeAndDeletedAtNotNull(String status, LocalDateTime lockedAtBefore);

    List<AppointmentEntity> findAllByStatusAndDateAndStartHour(String status, LocalDate date, LocalTime startHour);

    List<AppointmentEntity> findAllByStatusAndDateBefore(String status, LocalDate dateBefore);

    List<AppointmentEntity> findAllByDoctor_IdAndPatient_IdAndDeletedAtIsNull(UUID doctorId, UUID patientId);
}
