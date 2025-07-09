package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.PatientEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, UUID> {
    boolean existsByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    Optional<PatientEntity> findByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    List<PatientEntity> findAllByUser_IdAndIsMainAccount(UUID userId, boolean mainAccount);

    @Query("""
    SELECT DISTINCT a.patient
    FROM AppointmentEntity a
    WHERE a.doctor.id = :doctorId
      AND (:name IS NULL OR LOWER(CONCAT(a.patient.firstName, ' ', a.patient.lastName)) LIKE LOWER(CONCAT('%', :name, '%')))
      AND a.patient.deletedAt IS NULL
    ORDER BY a.patient.lastName ASC
""")
    List<PatientEntity> searchByDoctorAndNameFromAppointments(
            @Param("doctorId") UUID doctorId,
            @Param("name") String name,
            Pageable pageable
    );
}
