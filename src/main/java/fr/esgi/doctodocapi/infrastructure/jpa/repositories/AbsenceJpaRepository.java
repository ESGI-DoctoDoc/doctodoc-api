package fr.esgi.doctodocapi.infrastructure.jpa.repositories;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.AbsenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AbsenceJpaRepository extends JpaRepository<AbsenceEntity, UUID> {
    List<AbsenceEntity> findAllByDoctor_Id(UUID doctorId);

    List<AbsenceEntity> findAllByDoctor_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(UUID doctorId, LocalDate startDate, LocalDate endDate);

    Page<AbsenceEntity> findAllByDoctor_IdAndStartDateGreaterThanEqual(UUID doctorId, LocalDate date, Pageable pageable);

    Page<AbsenceEntity> findAllByDoctor_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(UUID doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
