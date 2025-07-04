package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.CareTrackingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CareTrackingJpaRepository extends JpaRepository<CareTrackingEntity, UUID> {
    // doctor
    Page<CareTrackingEntity> findAllByCreator_Id(UUID doctorId, Pageable pageable);

    Optional<CareTrackingEntity> findByIdAndPatient_Id(UUID careTrackingId, UUID patientId);

    @Query(value = """
            SELECT * FROM cares_tracking c
            WHERE c.care_tracking_id = :careTrackingId
              AND (
                :doctorId = ANY (c.doctors)
                OR c.doctor_id = :doctorId
              )
            """, nativeQuery = true)
    Optional<CareTrackingEntity> findByIdAndDoctorAccess(@Param("careTrackingId") UUID careTrackingId,
                                                         @Param("doctorId") UUID doctorId);

    @Query(value = """
        SELECT * FROM cares_tracking c
        WHERE (:doctorId = ANY (c.doctors) OR c.doctor_id = :doctorId)
        """,
            countQuery = """
        SELECT COUNT(*) FROM cares_tracking c
        WHERE (:doctorId = ANY (c.doctors) OR c.doctor_id = :doctorId)
        """,
            nativeQuery = true)
    Page<CareTrackingEntity> findAllByDoctorAccess(@Param("doctorId") UUID doctorId, Pageable pageable);

    Optional<CareTrackingEntity> findByIdAndPatient_IdAndCreator_Id(UUID careTrackingId, UUID patientId, UUID doctorId);

    // patient
    Page<CareTrackingEntity> findAllByPatient_IdOrderByCreatedAtDesc(UUID patientId, Pageable pageable);

    List<CareTrackingEntity> findAllByPatient_IdAndClosedAtNullOrderByCreatedAtDesc(UUID patientId);

}
