package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AppointmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, UUID> {
    List<AppointmentEntity> findAllBySlot_IdAndDeletedAtIsNull(UUID slotId);

    Page<AppointmentEntity> findAllByPatient_User_IdAndStatusOrderByDateDesc(UUID userId, String status, Pageable pageable);

    Page<AppointmentEntity> findAllByPatient_User_IdAndStatusOrderByDateAsc(UUID userId, String status, Pageable pageable);

    Optional<AppointmentEntity> findFirstByPatient_User_IdAndStatusAndDateAfterOrderByDateAsc(
            UUID userId, String status, LocalDate now);

    Page<AppointmentEntity> findAllByDoctor_Id(UUID doctorId, Pageable pageable);

    boolean existsByDoctor_IdAndPatient_Id(UUID doctorId, UUID patientId);

    Page<AppointmentEntity> findAllByDoctor_IdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AppointmentEntity> findAllByDoctor_IdAndDateGreaterThanEqual(UUID doctorId, LocalDate date, Pageable pageable);

    int countByDoctor_Id(UUID doctorId);

    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM AppointmentEntity a WHERE a.doctor.id = :doctorId")
    int countDistinctPatientsByDoctorId(@Param("doctorId") UUID doctorId);

    Optional<AppointmentEntity> findByIdAndPatient_Id(UUID id, UUID patientId);
}
